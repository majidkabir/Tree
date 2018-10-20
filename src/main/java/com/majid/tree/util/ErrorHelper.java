package com.majid.tree.util;

/**
 * Created by Majid Ghaffuri on 10/20/2018.
 */
public class ErrorHelper {
    public static final int SUCCESS = 0;
    public static final int NODE_NOT_FOUND = 1;
    public static final int PARENT_NOT_FOUND = 2;
    public static final int ROOT_ALREADY_EXIST = 3;
    public static final int THIS_PROPERTIES_SHOULD_BE_NULL = 4;

    public static final String MESSAGE_SUCCESS = null;
    public static final String MESSAGE_NODE_NOT_FOUND = "ٔNode not found!";
    public static final String MESSAGE_PARENT_NOT_FOUND = "ٔParent node not found!";
    public static final String MESSAGE_ROOT_ALREADY_EXIST = "ٔOne root node already exist, and you can not create new root node!";
    public static final String MESSAGE_THIS_PROPERTIES_SHOULD_BE_NULL = "ٔThis properties can not set by user!";

    public static String getMessage(int errorCode){
        switch (errorCode){
            case SUCCESS:
                return MESSAGE_SUCCESS;
            case NODE_NOT_FOUND:
                return MESSAGE_NODE_NOT_FOUND;
            case PARENT_NOT_FOUND:
                return MESSAGE_PARENT_NOT_FOUND;
            case ROOT_ALREADY_EXIST:
                return MESSAGE_ROOT_ALREADY_EXIST;
            case THIS_PROPERTIES_SHOULD_BE_NULL:
                return MESSAGE_THIS_PROPERTIES_SHOULD_BE_NULL;
        }

        return null;
    }
}
