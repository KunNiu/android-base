package com.sktlab.android.base.result;


public class ResultImp implements Result {
    private Result.Code resultCode;
    private Object data;
    private String dataJson;
    private Result result;

    private ResultImp(Result result) {
        this.result = result;
    }

    private ResultImp(Result.Code resultCode) {
        this.resultCode = resultCode;
    }

    private ResultImp(Object data) {
        if (data instanceof Result.Code) {
            this.resultCode = (Result.Code) data;
        } else
            this.data = data;
    }

    private ResultImp(Result.Code resultCode, Object data) {
        if (data instanceof Result.Code) {
            throw new RuntimeException("ResultImp parm error.");
        }
        this.resultCode = resultCode;
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        if (result != null)
            return result.isSuccess();
        return resultCode != null && resultCode.code == Result.Code.SUCCESS;
    }

    @Override
    public Object getData() {
        if (result != null)
            return result.getData();
        return data;
    }

    @Override
    public String getDataJson() {
        if (result != null)
            return result.getDataJson();
        return null;
    }

    @Override
    public Code getCode() {
        if (result != null)
            return result.getCode();
        return resultCode;
    }

    @Override
    public String getMessage() {
        if (result != null)
            return result.getMessage();
        return null;
    }

    public static Result unknown() {
        return new ResultImp(new Code(Code.UNKNOWN));
    }

    public static Result success() {
        return new ResultImp(new Code(Code.SUCCESS));
    }

    public static Result success(Object data) {
        return new ResultImp(new Code(Code.SUCCESS), data);
    }

    public static Result failed() {
        return new ResultImp(new Code(Code.FAILED));
    }

    public static Result failed(Code code) {
        return new ResultImp(code);
    }

    public static Result from(Result result) {
        if (result == null)
            return unknown();
        return new ResultImp(result);
    }
}
