package compositionmachine.server;

import java.util.HashMap;

public enum ContentType {
    IMAGE_GIF("image", "gif"),
    IMAGE_JPEG("image", "jpeg"),
    IMAGE_PNG("image", "png"),
    IMAGE_TIFF("image", "tiff"),
    IMAGE_SVGXML("image", "svg+xml"),
    TEXT_PLAIN("text", "plain"),
    TEXT_CSS("text", "css"),
    TEXT_CSV("text", "csv"),
    TEXT_HTML("text", "html"),
    TEXT_JAVASCRIPT("text", "javascript"),
    TEXT_XML("text", "xml"),
    APPLICATION_WASM("application", "wasm");

    private static HashMap<String, ContentType> typeMap = constructMap();

    public final String mainType;
    public final String subType;

    private String cachedString = null;

    private ContentType(String mainType, String subType) {
        this.mainType = mainType;
        this.subType = subType;
    }

    @Override
    public String toString() {
        if (this.cachedString != null) {
            return this.cachedString;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mainType).append("/").append(this.subType);
            this.cachedString = sb.toString();
            return this.cachedString;
        }
    }

    private static HashMap<String, ContentType> constructMap() {
        HashMap<String, ContentType> contentMap = new HashMap<>();

        contentMap.put("wasm", APPLICATION_WASM);
        contentMap.put("gif", IMAGE_GIF);
        contentMap.put("jpg", IMAGE_JPEG);
        contentMap.put("jpeg", IMAGE_JPEG);
        contentMap.put("png", IMAGE_PNG);
        contentMap.put("tiff", IMAGE_TIFF);
        contentMap.put("svg", IMAGE_SVGXML);
        contentMap.put("css", TEXT_CSS);
        contentMap.put("csv", TEXT_CSV);
        contentMap.put("html", TEXT_HTML);
        contentMap.put("js", TEXT_JAVASCRIPT);
        contentMap.put("xml", TEXT_XML);

        contentMap.put("txt", TEXT_PLAIN);

        return contentMap;
    }

    public static ContentType getTypeByExtension(String fileExtension) {
        return typeMap.get(fileExtension.toLowerCase());
    }
}
