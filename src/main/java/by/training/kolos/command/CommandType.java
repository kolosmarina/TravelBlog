package by.training.kolos.command;

import by.training.kolos.command.impl.*;

public enum CommandType {
    OPEN_MAIN_PAGE {
        {
            this.command = new OpenMainPageCommand();
        }
    },
    OPEN_REGISTRATION_PAGE {
        {
            this.command = new OpenRegistrationPageCommand();
        }
    },
    REGISTRATION {
        {
            this.command = new RegistrationCommand();
        }
    },
    AUTHENTICATION {
        {
            this.command = new AuthenticationCommand();
        }
    },
    OPEN_LOGIN_PAGE {
        {
            this.command = new OpenLoginPageCommand();
        }
    },
    ADMINISTRATION {
        {
            this.command = new AdministrationCommand();
        }
    },
    CHANGE_USER_STATUS {
        {
            this.command = new ChangeUserStatusCommand();
        }
    },
    OPEN_POSTS_PAGE {
        {
            this.command = new OpenPostsPageCommand();
        }
    },
    OPEN_POST_PAGE {
        {
            this.command = new OpenPostPageCommand();
        }
    },
    OPEN_SUCCESS_DELETE_POST_PAGE {
        {
            this.command = new OpenSuccessDeletePageCommand();
        }
    },
    OPEN_PHOTO_PAGE {
        {
            this.command = new OpenPhotoPageCommand();
        }
    },
    SAVE_LIKE {
        {
            this.command = new SaveLikeCommand();
        }
    },
    DELETE_LIKE {
        {
            this.command = new DeleteLikeCommand();
        }
    },
    SAVE_COMMENT {
        {
            this.command = new SaveCommentCommand();
        }
    },
    DELETE_COMMENT {
        {
            this.command = new DeleteCommentCommand();
        }
    },
    OPEN_NEW_POST_PAGE {
        {
            this.command = new OpenNewPostPageCommand();
        }
    },
    SET_MAIN_PHOTO {
        {
            this.command = new SetMainPhotoCommand();
        }
    },
    DELETE_POST {
        {
            this.command = new DeletePostCommand();
        }
    },
    LOGOUT {
        {
            this.command = new LogoutCommand();
        }
    },
    OPEN_ERROR_PAGE {
        {
            this.command = new OpenErrorPageCommand();
        }
    },
    CHANGE_LOCALE {
        {
            this.command = new ChangeLocaleCommand();
        }
    };
    AbstractCommand command;

    public AbstractCommand getCurrentCommand() {
        return command;
    }
}
