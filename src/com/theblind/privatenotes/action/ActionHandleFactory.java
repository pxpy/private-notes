package com.theblind.privatenotes.action;

public class ActionHandleFactory {
    private ActionHandleFactory() {
    }

    public static ActionHandle getActionHandle(ActionHandle.Operate operate) {
        switch (operate) {
            case ADD:
                return new ActionHandle.AddActionHandle();
            case EDIT:
                return new ActionHandle.EditActionHandle();
            case COPY:
                return new ActionHandle.CopyActionHandle();
            case WRAP:
                return new ActionHandle.WrapActionHandle();
            case DEL:
                return new ActionHandle.DelActionHandle();
            case DETAILED:
                return new ActionHandle.DetailedActionHandle();
        }
        throw new RuntimeException("");

    }

}
