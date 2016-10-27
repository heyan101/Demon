package dmodule.imageCode;

import javax.servlet.http.HttpServletResponse;

import demon.service.http.protocol.JsonProtocol;
import demon.service.http.protocol.JsonReq;
import demon.service.http.protocol.JsonResp;
import demon.service.http.protocol.RetStat;
import demon.service.http.ApiGateway;

public class ImageCodeHttpApi {

    protected ImageCodeApi utilsApi;
    
    public ImageCodeHttpApi(ImageCodeApi utilsApi) {
        this.utilsApi = utilsApi;
    }    
    
    @ApiGateway.ApiMethod(protocol = JsonProtocol.class, option = JsonProtocol.BIN_OPTION)
    public JsonResp getQRCodeImg(JsonReq req) throws Exception {
        
        Integer w = req.paramGetInteger("width", false);
        Integer h = req.paramGetInteger("height", false);
        
        String content = req.paramGetString("content", true, true);
        
        w = w == null ? 200 : w;
        h = h == null ? 200 : h;
        
        returnImage(req.env.response);
        QRCode.buildQRCode(content, w, h, req.env.response.getOutputStream());
        
        req.env.stat = RetStat.OK;
        return null;
    }
    
    @ApiGateway.ApiMethod(protocol=JsonProtocol.class, option=JsonProtocol.BIN_OPTION)
    public JsonResp getValidateCodeImg(JsonReq req) throws Exception {
        String imageId = req.paramGetString("imageId", true);
        
        Integer w = req.paramGetInteger("width", false);
        Integer h = req.paramGetInteger("height", false);
        
        String code = ImageCodeApi.parseValidateCode(imageId);
        
        w = w == null ? ImageCodeConfig.validateCodeImgWidth : w;
        h = h == null ? ImageCodeConfig.validateCodeImgHeight : h;
        
        returnImage(req.env.response);
        ValidateCode vlc = new ValidateCode(w, h, ImageCodeConfig.validateCodeCount, ImageCodeConfig.validateCodeImgRandomLineCount, code);
        vlc.write(req.env.response.getOutputStream());
        
        req.env.stat = RetStat.OK;
        return null;
    }
    
    @ApiGateway.ApiMethod(protocol = JsonProtocol.class)
    public JsonResp getValidateCodeInfo(JsonReq req) throws Exception {
        
        JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap.putAll(utilsApi.createValidateCode(req.env));
        return resp;
    }
    
    @ApiGateway.ApiMethod(protocol=JsonProtocol.class, option=JsonProtocol.BIN_OPTION)
    public JsonResp pressWatermark(JsonReq req) throws Exception {
        String url = req.paramGetString("url", true);
        String text = req.paramGetString("text", true);
        String fontName = req.paramGetString("fontName", false);
        Integer fontSize = req.paramGetInteger("fontSize", false);
        Integer x = req.paramGetInteger("x", false);
        Integer y = req.paramGetInteger("y", false);
        Double alpha = req.paramGetDouble("alpha", false, true);
        
        returnImage(req.env.response);
        utilsApi.pressWatermark(req.env, url, text, fontName, fontSize, x, y, alpha);
        
        req.env.stat = RetStat.OK;
        return null;
    }
    
    public static void returnImage(HttpServletResponse response) {

        long adddaysM = 315360000;
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "max-age="+315360000);
        response.addDateHeader("Expires", System.currentTimeMillis() + adddaysM * 1000);

    }
}
