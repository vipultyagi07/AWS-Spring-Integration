package com.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileNameUtility {

    public static String getFileNameAccordingToTheFileType(String originalFilename) {
        // Extract file extension
        String fileExtension = getFileExtension(originalFilename);

        // Determine folder name based on file type
        String folderName = getFolderName(fileExtension);

        // Generate timestamp
        String timestamp = generateTimestamp();

        // Construct the new filename
        String newFileName = folderName + "/" + timestamp + "_" + originalFilename;

        return newFileName;
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "others"; // No extension found
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public static String getFolderName(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "png":
            case "jpg":
            case "jpeg":
            case "img":
            case "psd":
            case "tiff":
            case "bmp":
                return "images";
            case "pdf":
                return "pdf";
            case "xlsx":
            case "xls":
            case "csv":
                return "excel";
            case "pptx":
            case "ppt":
                return "ppt";
            case "mp4":
            case "avi":
            case "mkv":
            case "mpg":
            case "m4v":
                return "videos";
            case "mp3":
            case "wav":
            case "flac":
            case "m4a":
            case "wma":
            case "aac":
                return "audio";
            case "docx":
            case "doc":
            case "txt":
            case "rtf":
                return "documents";
            default:
                return "others";
        }
    }


    private static String generateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}
