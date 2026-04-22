package multimodalinterviewaiback.common;


import java.util.List;

public class BaseContext {

    private static ThreadLocal<String> currentUsername = new ThreadLocal<>();
    private static ThreadLocal<String> currentPassword = new ThreadLocal<>();
    private static ThreadLocal<String> currentType = new ThreadLocal<>();
    private static ThreadLocal<List<Integer>> currentImageIds = new ThreadLocal<>();
    private static ThreadLocal<Long> currentId = new ThreadLocal<>();

    public static void setCurrentId(Long id) {currentId.set(id);}

    public static void setCurrentUsername(String username) {
        currentUsername.set(username);
    }

    public static void setCurrentPassword(String password) {
        currentPassword.set(password);
    }

    public static void setCurrentType(String type) {
        currentType.set(type);
    }

    public static void setCurrentImageIds(List<Integer> ids) {
        currentImageIds.set(ids);
    }

    public static Long getCurrentId() {return currentId.get();}

    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    public static String getCurrentPassword() {
        return currentPassword.get();
    }

    public static String getCurrentType() {
        return currentType.get();
    }

    public static List<Integer> getCurrentImageIds() {
        return currentImageIds.get();
    }

    public static void removeCurrentUsername() {
        currentUsername.remove();
    }

    public static void removeCurrentPassword() {
        currentPassword.remove();
    }

    public static void removeCurrentType() {
        currentType.remove();
    }

    public static void removeCurrentImageIds() {
        currentImageIds.remove();
    }

}