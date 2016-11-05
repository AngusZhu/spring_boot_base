package com.sinosafe.payment.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

import net.coobird.thumbnailator.Thumbnailator;
import org.jasig.cas.client.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.multipart.MultipartFile;


public class FileUpload {
    private static Logger logger = LoggerFactory.getLogger(FileUpload.class);
    public final static String SERVER_BASE_PATH_USER = "image.server.profile";
    public final static String SERVER_BASE_PATH_MATERIAL = "image.server.material";
    public final static String SERVER_BASE_PATH_TRANSACTION = "image.server.transaction";
    public final static String SERVER_BASE_PATH_COURSE = "image.server.course";
    public final static String SERVER_BASE_PATH_RESTAURANT = "image.server.restaurant";
    public final static String SERVER_BASE_PATH_NOTIFICATION = "notification.server.material";
    public final static String SERVER_BASE_PATH_GUESTROOM = "image.server.guestroom";
    public final static String SERVER_BASE_PATH_ADVERTISE = "image.server.advertise";
    public final static String SERVER_BASE_PATH_NEWS = "image.server.notice";
    private final static String THUMBNAIL_SUFFIX = "_small";

    public static enum FileCategory {
        USER(SERVER_BASE_PATH_USER),
        MATERIAL(SERVER_BASE_PATH_MATERIAL),
        TRANSACTION(SERVER_BASE_PATH_TRANSACTION),
        COURSE(SERVER_BASE_PATH_COURSE),
        RESTAURANT(SERVER_BASE_PATH_RESTAURANT),
        NOTIFICATION(SERVER_BASE_PATH_NOTIFICATION),
        GUESTROOM(SERVER_BASE_PATH_GUESTROOM),
        ADVERTISE(SERVER_BASE_PATH_ADVERTISE),
        NOTICE(SERVER_BASE_PATH_NEWS);
        private final String configName;

        FileCategory(String configName) {
            this.configName = configName;
        }

        public String getConfigName() {
            return this.configName;
        }
    }

    public static String upload(MultipartFile ufile, FileCategory servertype) throws Exception {
        String fpath = getBasePath(servertype);
        return upload(ufile, fpath, null, false);
    }

    /*if(isImage){
    //indicate what type this upload file is. It can be picture,pdf,...
    presentMaterial.setMaterialType(MEDIA_IMAGE);
    String thumbnailName= filePrefix + THUMBNAIL_SUFFIX +fileSuffix;
    File thumbnailFile = new File(path, thumbnailName);
    Thumbnailator.createThumbnail(targetFile, thumbnailFile, length, width);
    }*/
    public static String upload(MultipartFile ufile, FileCategory servertype, String newName, boolean isThumbnail) throws Exception {
        String fpath = getBasePath(servertype);
        return upload(ufile, fpath, newName, false, isThumbnail);
    }

    public static String getBasePath(FileCategory servertype) throws Exception {
        return getPropertyValue(servertype.getConfigName());
    }

    private static String getPropertyValue(String key) throws Exception {
        Resource resource = new ClassPathResource("placeholder/app.properties");
        Properties appProps = PropertiesLoaderUtils.loadProperties(resource);
        String fpath = appProps.getProperty(key);
        return fpath;
    }

    /**
     * upload a file to file server
     *
     * @param ufile   file to be uploaded
     * @param fpath   server path
     * @param replace true - replace existing file
     * @return new file name
     * @throws Exception
     */
    public static String upload(MultipartFile ufile, String fpath) throws Exception {
        return upload(ufile, fpath, null, false);
    }

    public static String upload(MultipartFile ufile, String fpath, String newName, boolean replace) throws Exception {
        return upload(ufile, fpath, newName, replace, false);
    }

    /**
     * upload a file to file server
     *
     * @param ufile   file to be uploaded
     * @param fpath   server path
     * @param newName The file name to be stored. Null will be random name
     * @param replace true - replace existing file
     * @return new file name
     * @throws Exception
     */
    public static String upload(MultipartFile ufile, String fpath, String newName, boolean replace, boolean isThumbnail)
            throws Exception {
        if (ufile == null || ufile.isEmpty()) {
            logger.info("File is empty!");
            return "{\"warning\":\"File is empty!\"}";
        }
// create remote directory
        File dir = new File(fpath);
        if (!dir.exists()) {
            boolean isCreated = dir.mkdirs();
            if (!isCreated) {
                logger.info("Can't create folder!");
                return "{\"warning\":\"Can't create folder!\"}";
            }
        }
// create destination file
        File destFile = null;
        if (CommUtil.nvl(newName).length() == 0) {
            String orinalName = ufile.getOriginalFilename();
            destFile = createNewFile(dir, orinalName);
        } else {
            destFile = new File(dir, newName);
            if (destFile.exists() && !replace) {
                throw new Exception("file already exists: " + destFile.getAbsolutePath());
            }
            replaceFile(destFile);
        }
        // copy the file
        InputStream inputStream = ufile.getInputStream();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(destFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        String absolutePath = destFile.getAbsolutePath();
        if (isThumbnail) {
            String[] s = absolutePath.split("\\.");
            File thumbnailFile = new File(s[0] + THUMBNAIL_SUFFIX + "." + s[1]);
            int width = Integer.valueOf(getPropertyValue("thumbnail.length"));
            int length = Integer.valueOf(getPropertyValue("thumbnail.width"));
            Thumbnailator.createThumbnail(destFile, thumbnailFile, length, width);
        }
        return absolutePath;
    }

    private static void replaceFile(File destFile) throws Exception {
// replace the file.
        if (destFile.exists()) {
            destFile.delete();
        }
//retry to create new file
        boolean createDone = false;
        int retryCount = 10;
        do {
            createDone = destFile.createNewFile();
            retryCount--;
            if (!createDone) {
                Thread.sleep(100);
            }
        } while (!createDone && retryCount > 0);
        if (!createDone) {
            throw new Exception("file can't be replaced: " + destFile.getAbsolutePath());
        }
    }

    /**
     * create new file with random name
     *
     * @param dir
     * @param orinalName
     * @return
     * @throws Exception
     */
    private static File createNewFile(File dir, String orinalName) throws Exception {
        String suffix = orinalName.substring(orinalName.lastIndexOf("."));
        File destFile = null;
        do {
            String destFileName = UUID.randomUUID().toString().replace("-", "_") + suffix;
            destFile = new File(dir, destFileName);
        } while (destFile.exists());
        return destFile;
    }

    public static void deleteFile(String filename, String folder) throws Exception {
        File file = new File(folder + "/" + filename);
        String[] s = filename.split("\\.");
        File thumbnailFile = new File(folder + "/" + s[0] + THUMBNAIL_SUFFIX + "." + s[1]);
        if (file.exists()) {
            file.delete();
        }
        if (thumbnailFile.exists()) {
            thumbnailFile.delete();
        }
    }
}