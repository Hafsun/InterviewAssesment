package multimodalinterviewaiback.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@Slf4j
public class QuestionBankParser {

    // 仅允许的格式（保持原有逻辑不变）
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
            Arrays.asList(".pdf", ".docx", ".xlsx", ".doc", ".xls")
    );

    // 注入大模型客户端
    @Autowired
    private LLMQuestionBankClient llmQuestionBankClient;

    /**
     * 解析题库文件（对外方法完全不变，业务层无需修改）
     */
    public Map<String, String> parseQuestionBank(MultipartFile file) {
        Map<String, String> questionAnswerMap = new HashMap<>();
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            throw new RuntimeException("文件名称格式错误，无扩展名");
        }
        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 校验仅允许的格式（保持原有逻辑不变）
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new RuntimeException("仅支持pdf、docx、xlsx、doc、xls格式文件，当前格式：" + ext);
        }

        try (InputStream inputStream = file.getInputStream()) {
            // 步骤1：提取文件中的原始文本（保持原有文件读取逻辑不变）
            String fileText = extractTextFromFile(inputStream, ext);

            // 步骤2：调用大模型解析文本（核心修改：替换原有正则解析为AI解析）
            questionAnswerMap = llmQuestionBankClient.parseByLLM(fileText);

            log.info("题库AI解析完成：{}（{}），共抽取 {} 道题目", fileName, ext, questionAnswerMap.size());
        } catch (IOException e) {
            log.error("解析题库文件失败", e);
            throw new RuntimeException("解析文件失败：" + e.getMessage());
        }
        return questionAnswerMap;
    }

    /**
     * 提取不同格式文件的原始文本（复用原有文件读取逻辑，仅提取文本，不解析）
     */
    private String extractTextFromFile(InputStream inputStream, String ext) throws IOException {
        StringBuilder text = new StringBuilder();
        switch (ext) {
            case ".pdf":
                try (PDDocument document = PDDocument.load(inputStream)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    text.append(stripper.getText(document));
                }
                break;
            case ".docx":
                try (XWPFDocument document = new XWPFDocument(inputStream)) {
                    for (XWPFParagraph paragraph : document.getParagraphs()) {
                        text.append(paragraph.getText()).append("\n");
                    }
                }
                break;
            case ".doc":
                try (HWPFDocument document = new HWPFDocument(inputStream);
                     WordExtractor extractor = new WordExtractor(document)) {
                    String[] paragraphs = extractor.getParagraphText();
                    for (String para : paragraphs) {
                        if (para != null && !para.trim().isEmpty()) {
                            text.append(para.trim()).append("\n");
                        }
                    }
                }
                break;
            case ".xlsx":
            case ".xls":
                // Excel文件：拼接所有单元格文本（按行拼接）
                try (Workbook workbook = ext.equals(".xlsx") ? new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row : sheet) {
                        for (Cell cell : row) {
                            text.append(getCellValue(cell)).append("\t");
                        }
                        text.append("\n");
                    }
                }
                break;
            default:
                throw new RuntimeException("不支持的文件格式：" + ext);
        }
        // 清理文本中的多余空白（避免大模型解析干扰）
        return text.toString().replaceAll("\\s+", " ").trim();
    }

    /**
     * 获取Excel单元格值（复用原有逻辑）
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toString();
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            default:
                return "";
        }
    }
}