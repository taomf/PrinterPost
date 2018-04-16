package cn.speedpay.s.xedj.bean;

/**
 * Created by Administrator on 2016/8/22.
 */
public class CodeBean {

    public String CodeType;//code类型
    public String CodeId;//codeID
    public String CodeDesc;//code描述

    public CodeBean(){

    }

    public CodeBean(String CodeId,String CodeDesc){
        this.CodeId = CodeId;
        this.CodeDesc = CodeDesc;
    }

    public CodeBean(String CodeType,String CodeId,String CodeDesc){
        this.CodeType = CodeType;
        this.CodeId = CodeId;
        this.CodeDesc = CodeDesc;
    }
    /**
     * 字典类型
     * @return
     */
    public String getCodeType() {
        return CodeType;
    }
    /**
     * 字典类型
     * @param codeType
     */
    public void setCodeType(String codeType) {
        CodeType = codeType;
    }
    /**
     * 字典ID
     * @return
     */
    public String getCodeId() {
        return CodeId;
    }
    /**
     * 字典ID
     * @param codeId
     */
    public void setCodeId(String codeId) {
        CodeId = codeId;
    }
    /**
     * 字典描述
     * @return
     */
    public String getCodeDesc() {
        return CodeDesc;
    }
    /**
     * 字典描述
     * @param codeDesc
     */
    public void setCodeDesc(String codeDesc) {
        CodeDesc = codeDesc;
    }
}
