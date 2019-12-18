package by.training.kolos.command;

/**
 * Класс для хранения констант приложения
 *
 * @author Колос Марина
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final String URL_KEY = "db.url";
    public static final String USER_KEY = "db.username";
    public static final String PASSWORD_KEY = "db.password";
    public static final String SIZE_POOL = "db.pool.size";
    public static final String PHOTO_STORAGE = "db.photo.storage";
    public static final String DATA_STORAGE_FOR_LETTERS = "data.storage.for.letters";

    public static final String CONFIG_FILE_NAME = "config";
    public static final String MESSAGES_FILE_NAME = "messages";

    public static final String SEPARATOR_FOR_TAGS = "#";
    public static final String SEPARATOR_FOR_PARAMS = ":";
    public static final String JS_TAG = "</?script>";
    public static final String EMPTY_STRING = "";
    public static final String NAME_FOR_TAGS = "tags-input:";
    public static final String UNDERSCORE = "_";

    public static final String PARAM_ENCODING = "encoding";
    public static final String PARAM_COMMAND = "command";
    public static final String PARAM_LOCALE_EN_US = "en_US";
    public static final String PARAM_LOCALE_RU_RU = "ru_RU";
    public static final String PARAM_LOCALE_BE_BY = "be_BY";
    public static final String PARAM_LOCALE = "locale";
    public static final String PARAM_WORLD_PARTS = "worldParts";
    public static final String PARAM_WORLD_PART = "worldPart";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_OWN = "own";
    public static final String PARAM_USERS = "users";
    public static final String PARAM_USER = "user";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_POPULAR_USER_ID = "popularUserId";
    public static final String PARAM_POPULAR_TAG_ID = "popularTagId";
    public static final String PARAM_POST_ID = "postId";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_PHOTO_ID = "photoId";
    public static final String PARAM_PHOTOS = "photos";
    public static final String PARAM_PHOTO = "photo";
    public static final String PARAM_MAIN_PHOTO = "mainPhoto";
    public static final String PARAM_NEXT_PHOTO_ID = "nextPhotoId";
    public static final String PARAM_PREV_PHOTO_ID = "prevPhotoId";
    public static final String PARAM_HAS_NEXT_PHOTO = "hasNextPhoto";
    public static final String PARAM_HAS_PREV_PHOTO = "hasPrevPhoto";
    public static final String PARAM_PHOTOS_NUMBER_AVAILABLE_FOR_UPLOAD = "photosNumberAvailableForUpload";
    public static final String PARAM_IS_ACTIVE_USER = "isActiveUser";
    public static final String PARAM_POSTS = "posts";
    public static final String PARAM_POST_NAME = "postName";
    public static final String PARAM_POST_USER_ID = "postUserId";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_NICKNAME = "nickname";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_LIKE = "like";
    public static final String PARAM_LIKES_NUMBER = "likesNumber";
    public static final String PARAM_TAGS = "tags";
    public static final String PARAM_COMMENTS = "comments";
    public static final String PARAM_IS_NEW_POST = "isNewPost";
    public static final String PARAM_VALUE_COMMENT = "valueComment";
    public static final String PARAM_EMPTY_COMMENT = "emptyComment";
    public static final String PARAM_COMMENT_ID = "commentId";
    public static final String PARAM_SORT_BY = "sortBy";
    public static final String PARAM_PAGE_NUMBER = "pageNumber";
    public static final String PARAM_LAST_PAGE_NUMBER = "lastPageNumber";
    public static final String PARAM_PREV_PAGE_NUMBER = "prevPageNumber";
    public static final String PARAM_NEXT_PAGE_NUMBER = "nextPageNumber";
    public static final String PARAM_INVALID_EMAIL = "invalidEmail";
    public static final String PARAM_INVALID_PASSWORD = "invalidPassword";
    public static final String PARAM_INVALID_NICKNAME = "invalidNickname";
    public static final String PARAM_SUCCESS_REGISTRATION = "successRegistration";
    public static final String PARAM_ERROR_REGISTRATION = "errorRegistration";
    public static final String PARAM_USER_BLOCKED = "blockedPassMessage";
    public static final String PARAM_LOGIN_OR_PASSWORD_ERROR = "loginOrPasswordError";
    public static final String PARAM_EMPTY_POST_NAME = "emptyPostName";
    public static final String PARAM_EMPTY_FILES = "emptyFiles";

    public static final String PAGE_INDEX = "path.page.index";
    public static final String PAGE_MAIN = "path.page.main";
    public static final String PAGE_ERROR = "path.page.error";
    public static final String PAGE_ADMINISTRATION = "path.page.administration";
    public static final String PAGE_REGISTRATION = "path.page.registration";
    public static final String PAGE_LOGIN = "path.page.login";
    public static final String PAGE_ALL_POSTS = "path.page.allPosts";
    public static final String PAGE_POST = "path.page.post";
    public static final String PAGE_PHOTO = "path.page.photo";
    public static final String PAGE_NEW_POST = "path.page.newPost";
    public static final String PAGE_SUCCESS_DELETE_POST = "path.page.successDeletePost";
    public static final String MAIN_SERVLET = "path.page.controller";

    public static final String MESSAGE_USER_BLOCKED = "message.userBlocked";
    public static final String MESSAGE_LOGIN_OR_PASSWORD_INCORRECT = "message.loginOrPasswordError";
    public static final String MESSAGE_REGISTRATION_INVALID_EMAIL_ERROR = "message.registrationInvalidEmailError";
    public static final String MESSAGE_REGISTRATION_NOT_UNIQUE_EMAIL_ERROR = "message.registrationNotUniqueEmailError";
    public static final String MESSAGE_REGISTRATION_INVALID_PASSWORD_ERROR = "message.registrationInvalidPasswordError";
    public static final String MESSAGE_REGISTRATION_INVALID_NICKNAME_ERROR = "message.registrationInvalidNicknameError";
    public static final String MESSAGE_REGISTRATION_NOT_UNIQUE_NICKNAME_ERROR = "message.registrationNotUniqueNicknameError";
    public static final String MESSAGE_REGISTRATION_ERROR = "message.registrationError";
    public static final String MESSAGE_SUCCESS_REGISTRATION = "message.successRegistration";
    public static final String MESSAGE_EMPTY_POST_NAME = "message.emptyPostName";
    public static final String MESSAGE_EMPTY_FILES = "message.emptyFiles";
    public static final String MESSAGE_EMPTY_COMMENT = "message.emptyComment";
    public static final String MESSAGE_TITLE_LETTER = "message.registrationLetterTitle";
    public static final String MESSAGE_BODY_LETTER = "message.registrationLetterBody";

    public static final String COMMAND_OPEN_MAIN_PAGE = "open_main_page";
    public static final String COMMAND_OPEN_REGISTRATION_PAGE = "open_registration_page";
    public static final String COMMAND_ADMINISTRATION = "administration";
    public static final String COMMAND_CHANGE_USER_STATUS = "change_user_status";
    public static final String COMMAND_OPEN_POST_PAGE = "open_post_page";
    public static final String COMMAND_OPEN_SUCCESS_DELETE_POST_PAGE = "open_success_delete_post_page";
    public static final String COMMAND_OPEN_PHOTO_PAGE = "open_photo_page";
    public static final String COMMAND_SAVE_LIKE = "save_like";
    public static final String COMMAND_DELETE_LIKE = "delete_like";
    public static final String COMMAND_OPEN_NEW_COMMENT_PAGE = "open_new_comment_page";
    public static final String COMMAND_SAVE_COMMENT = "save_comment";
    public static final String COMMAND_DELETE_COMMENT = "delete_comment";
    public static final String COMMAND_OPEN_NEW_POST_PAGE = "open_new_post_page";
    public static final String COMMAND_SET_MAIN_PHOTO = "set_main_photo";
    public static final String COMMAND_DELETE_POST = "delete_post";
    public static final String COMMAND_LOGOUT = "logout";
    public static final String COMMAND_OPEN_ERROR_PAGE = "open_error_page";
}
