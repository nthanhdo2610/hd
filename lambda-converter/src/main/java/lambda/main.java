package lambda;

import java.util.Date;
import java.io.File;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

public class main {
    public static void main(String[] args) throws Exception{
        // Initialise
        XComponentContext xContext = Bootstrap.bootstrap();

        XMultiComponentFactory xMCF = xContext.getServiceManager();

        Object oDesktop = xMCF.createInstanceWithContext(
                "com.sun.star.frame.Desktop", xContext);

        XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(
                XDesktop.class, oDesktop);

        XComponentLoader xCompLoader = (XComponentLoader) UnoRuntime
                .queryInterface(com.sun.star.frame.XComponentLoader.class, xDesktop);

        String sUrl = "file:///Ban_Dieu_Khoan_Va_Dieu_Kien_Chung.docx";

        PropertyValue[] propertyValues = new PropertyValue[0];

        propertyValues = new PropertyValue[1];
        propertyValues[0] = new PropertyValue();
        propertyValues[0].Name = "Hidden";
        propertyValues[0].Value = new Boolean(true);

        XComponent xComp = xCompLoader.loadComponentFromURL(
                sUrl, "_blank", 0, propertyValues);

        // save as a PDF
        XStorable xStorable = (XStorable) UnoRuntime
                .queryInterface(XStorable.class, xComp);

        propertyValues = new PropertyValue[2];
        propertyValues[0] = new PropertyValue();
        propertyValues[0].Name = "Overwrite";
        propertyValues[0].Value = new Boolean(true);
        propertyValues[1] = new PropertyValue();
        propertyValues[1].Name = "FilterName";
        propertyValues[1].Value = "writer_pdf_Export";

// Appending the favoured extension to the origin document name
        String myResult = "letterOutput.pdf";
        xStorable.storeToURL("file:///" + myResult, propertyValues);

        System.out.println("Saved " + myResult);

        // shutdown
        xDesktop.terminate();
    }
}
