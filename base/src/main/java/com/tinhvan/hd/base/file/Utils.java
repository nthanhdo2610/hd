package com.tinhvan.hd.base.file;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.Config;
import com.tinhvan.hd.base.HDConfig;
import com.tinhvan.hd.base.InternalServerErrorException;
import org.apache.tika.Tika;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Utils {

    /***
     * Check data base64 of file is valid or not
     *
     * @param data string base64
     * @return byte arrays of file result
     */
    public static byte[] validateData(String data) {
        if (StringUtils.isEmpty(data))
            return null;
        Config config = HDConfig.getInstance();
        byte[] base64Bytes = Base64
                .getDecoder()
                .decode(data);
        int max = Integer.valueOf(config.get("MAX_FILE_SIZE_UPLOAD_MB")) * 1024 * 1024;
        if (base64Bytes.length > max)
            throw new BadRequestException(1128);
        /*Tika tika = new Tika();
        String contentType = tika.detect(base64Bytes);
        String type = contentType.split("/")[0];
        if (type.equals("image") && !contentType.equals(MimeTypes.MIME_IMAGE_GIF)) {
            try {
                //Read the image file and store as a BufferedImage
                ByteArrayInputStream bis = new ByteArrayInputStream(base64Bytes);
                BufferedImage convertMe = ImageIO.read(bis);
                bis.close();

                //resize image uploaded
                int width = convertMe.getWidth();
                int height = convertMe.getHeight();
                if (width > 800) {
                    width = 800;
                    height = (width * convertMe.getHeight()) / convertMe.getWidth();

                    //Save the BufferedImage object
                    BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    result.createGraphics().drawImage(resize(convertMe, width, height), 0, 0, Color.WHITE, null);

                    //Write BufferedImage has converted and parse to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(result, MimeTypes.lookupExtension(contentType), bos);
                    base64Bytes = bos.toByteArray();
                    bos.close();
                }

            } catch (IOException e) {
                throw new InternalServerErrorException(e.getMessage());
            }

        }*/
        return base64Bytes;
    }

    /**
     * Resize a picture with width height request
     *
     * @param img    image need to resize
     * @param width
     * @param height
     * @return BufferedImage is image has resized
     */
    private static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
