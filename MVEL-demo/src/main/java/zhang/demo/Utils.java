package zhang.demo;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

public class Utils {

    public  String readTemplate(String fileName, Map params) throws IOException {
        // Read the template file

        InputStream tStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

        Scanner tScanner = new Scanner(tStream);
        String template = tScanner.useDelimiter("\\Z").next();
        tScanner.close();
        tStream.close();



        // Render the template
        CompiledTemplate t = TemplateCompiler.compileTemplate(template);

        // Execute the template with parameters, as needed.
        if(params.isEmpty()) {
            template = (String)TemplateRuntime.execute(t);
        }
        else {
            template = (String)TemplateRuntime.execute(t, params);
        }

        return template;
    }
}
