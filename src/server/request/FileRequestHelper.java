package server.request;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.MultipartStream;

import com.sun.net.httpserver.Headers;

public class FileRequestHelper {
    public static Map<String, File> findAllMultipartFiles(IHttpRequest request) throws IOException {
        Headers headers = request.getHeaders();
        String contentTypeHeader = headers.getFirst("Content-Type");
        String contentLengthHeader = headers.getFirst("Content-Length");
        Map<String, String> contentTypeHeaderFields = extractFields(contentTypeHeader);

        ByteArrayInputStream input = new ByteArrayInputStream(request.getRequestBody().readAllBytes());

        Map<String, File> fileMap = new HashMap<>();
        MultipartStream multipartStream = new MultipartStream(input,
                contentTypeHeaderFields.get("boundary").getBytes(),
                Integer.valueOf(contentLengthHeader),
                null);

        boolean nextPart = multipartStream.skipPreamble();
        while (nextPart) {
            // process headers
            String fileHeaders = multipartStream.readHeaders();
            if (fileHeaders != null) {
                Map<String, String> allHeaders = splitHeaders(fileHeaders);
                String inputFieldName = extractFields(allHeaders.get("Content-Disposition")).get("name");
                String inputFileName = extractFields(allHeaders.get("Content-Disposition")).get("filename");

                String fileName = inputFileName.replace("\"", "");
                String fieldName = inputFieldName.replace("\"", "");
                File newFile = new File("build/tmp/sound-" + System.currentTimeMillis() + "-" + fieldName + "-" + fileName);

                FileOutputStream output = new FileOutputStream(newFile);
                multipartStream.readBodyData(output);

                fileMap.put(fieldName, newFile);
            }
            nextPart = multipartStream.readBoundary();
        }

        return fileMap;
    }

    // ref:
    // http://www.java2s.com/example/java-src/pkg/io/selendroid/testapp/webdrivertestserver/handler/uploadfilehandler-80a53.html
    private static Map<String, String> splitHeaders(String readHeaders) {
        HashMap<String, String> map = new HashMap<>();
        String[] headers = readHeaders.split("\r\n");
        for (String headerLine : headers) {
            int index = headerLine.indexOf(':');
            if (index < 0) {
                continue;
            }
            String key = headerLine.substring(0, index);
            String value = headerLine.substring(index + 1).trim();
            map.put(key, value);
        }
        return map;
    }

    private static Map<String, String> extractFields(String contentTypeHeader) {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] contentTypeHeaderParts = contentTypeHeader.split("[;,]");
        for (String contentTypeHeaderPart : contentTypeHeaderParts) {
            String[] kv = contentTypeHeaderPart.split("=");
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }
}
