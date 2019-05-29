//package com.zhupeng.baseframe.config;
//
//import io.github.robwin.markup.builder.MarkupLanguage;
//import io.github.robwin.swagger2markup.GroupBy;
//import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
//import io.swagger.io.HttpClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//import java.io.InputStream;
//
//@Slf4j
//@Component
//public class SwaggerOfflineFileBorn implements ApplicationRunner {
//    @Override
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//        int2();
//    }
//    /**
//     * 服务器地址
//     */
//    private static String SERVICE_URL = "http://127.0.0.1:9001";
//
//    static {
//        StreamTool.checkFile(Constant.OUTPUT_DIR);
//    }
//
//
////    @PostConstruct
//    public static void  int2(){
//        try {
//            log.info("=================================================开始中…======================");
//            System.out.println("=================================================开始中……");
//            outputJson();
//            // 这个outputDir必须和插件里面<generated></generated>标签配置一致
//            Swagger2MarkupConverter.from(Constant.FILE_PATH)
//                    .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
//                    .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
//                    .withExamples(Constant.SNIPPET_DIR)
//                    .build()
//                    .intoFolder(Constant.OUTPUT_DIR);// 输出
//            System.out.println("==================================================结束了！");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void outputJson() throws Exception {
//        String url = SERVICE_URL + Constant.URI;
//        HttpClient httpClient = new HttpClient(url);
//        System.out.println("开始请求:" + url);
//        InputStream ipputStream = httpClient.execute();
//        byte[] data = StreamTool.read(ipputStream);
//        System.out.println("请求结果:" + new String(data, "UTF-8"));
//        StreamTool.saveDataToFile(data, Constant.FILE_PATH);
//        System.out.println("请求结果已成功保存到本地:" + Constant.FILE_PATH);
//
//    }
//}
