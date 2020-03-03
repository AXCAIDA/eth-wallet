package io.acrosafe.wallet.eth.web.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Response
{
    @JsonProperty("result_code")
    private Integer resultCode;

    @JsonProperty("result")
    private Result result;

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }
}
