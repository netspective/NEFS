package auto.dal.db.enum;


public final class SessionStatusType
{
    public final int ACTIVE = 0;
    public final int INACTIVE__SYSTEM_FORCED_LOGOUT_ = 3;
    public final int INACTIVE__TIMEOUT_ = 4;
    public final int INACTIVE__USER_FORCED_LOGOUT_ = 2;
    public final int INACTIVE__USER_NORMAL_LOGOUT_ = 1;
}