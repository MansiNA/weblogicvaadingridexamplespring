package de.dbuss.vaadindemospring;

public class MonitoringEntry {

    public MonitoringEntry(Long id, String sql, String titel, String beschreibung, String handlungsInfo, Integer checkIntervall,
                           Integer warningSchwellwert, Integer errorSchwellwert, Boolean isActive, String sqlDetail) {
        this.id=id;
        this.sql=sql;
        this.titel=titel;
        this.beschreibung=beschreibung;
        this.handlungsInfo=handlungsInfo;
        this.checkIntervall=checkIntervall;
        this.warningSchwellwert=warningSchwellwert;
        this.errorSchwellwert=errorSchwellwert;
        this.isActive=isActive;
        this.sqlDetail=sqlDetail;
    }
    private Long id;

    private String sql;

    private String titel;

    private String beschreibung;

    private String handlungsInfo;

    private Integer checkIntervall;

    private Integer warningSchwellwert;

    private Integer errorSchwellwert;

    private Boolean isActive;

    private String sqlDetail;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getHandlungsInfo() {
        return handlungsInfo;
    }

    public void setHandlungsInfo(String handlungsInfo) {
        this.handlungsInfo = handlungsInfo;
    }

    public Integer getCheckIntervall() {
        return checkIntervall;
    }

    public void setCheckIntervall(Integer checkIntervall) {
        this.checkIntervall = checkIntervall;
    }

    public Integer getWarningSchwellwert() {
        return warningSchwellwert;
    }

    public void setWarningSchwellwert(Integer warningSchwellwert) {
        this.warningSchwellwert = warningSchwellwert;
    }

    public Integer getErrorSchwellwert() {
        return errorSchwellwert;
    }

    public void setErrorSchwellwert(Integer errorSchwellwert) {
        this.errorSchwellwert = errorSchwellwert;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSqlDetail() {
        return sqlDetail;
    }

    public void setSqlDetail(String sqlDetail) {
        this.sqlDetail = sqlDetail;
    }
}
