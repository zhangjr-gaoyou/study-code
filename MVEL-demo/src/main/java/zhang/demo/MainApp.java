package zhang.demo;


import org.mvel2.MVEL;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class MainApp {


    public static void main(String[] args) {

        EntityServiceDemo demoService = new EntityServiceDemo();
        List<EntityDemo> demos = demoService.getEntityDemos();


        Map vars = new HashMap();
        vars.put( "demos", demos );
        String code = "count = 0; "+
                "sum = 0; "+
                "foreach(demo : demos) { " +
                "    count += demo.p3;" +
                "    sum += demo.p2;"+
                "} " +
                "if ((count >= 800) && (sum >= 3)){" +
                "   return true;" +
                "}" +
                "else { " +
                "   return false;" +
                "}";

        Boolean output = (Boolean)MVEL.eval(code,vars) ;
        System.out.println("MVEL - The result: " + output);


        Map params = new HashMap();
        params.put("count",500);
        params.put("sum",4);


        Utils util = new Utils();

        try {
            System.out.println("Script: " + util.readTemplate("rule1.tmpl", params));
        }catch (Exception e){
            e.printStackTrace();
        }

    }








}
