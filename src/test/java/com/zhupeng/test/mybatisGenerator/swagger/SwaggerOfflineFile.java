package com.zhupeng.test.mybatisGenerator.swagger;

import com.zhupeng.baseframe.config.StreamTool;
import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import io.swagger.io.HttpClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerOfflineFile {
    @Autowired
    private MockMvc mockMvc;

//    private String snippetDir = "target/generated-snippets";
//    private String outputDir  = "target/asciidoc";

    private static String SERVICE_URL = "http://127.0.0.1:9001";
    public static final String SNIPPET_DIR = "target/generated-snippets";
    public static final String OUTPUT_DIR = "target/asciidoc";
    public static final String URI = "/v2/api-docs";
    public static final String FILE_NAME = "swagger.json";
    public static final String FILE_PATH = OUTPUT_DIR + "/" + FILE_NAME;

    @After
    public void Test() throws Exception{
        // 得到swagger.json,写入outputDir目录中
//        mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_URL+URI).accept(MediaType.APPLICATION_JSON))
//                .andDo(SwaggerResultHandler.outputDirectory(OUTPUT_DIR).build())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        // 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
//        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
//        Swagger2MarkupConverter.from(FILE_PATH)
//                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
//                .withExamples(SNIPPET_DIR)
//                .build()
//                .intoFolder(OUTPUT_DIR);// 输出
    }
    @Test
    public void test() throws Exception {
        outputJson();
        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
        Swagger2MarkupConverter.from(FILE_PATH)
                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
                .withExamples(SNIPPET_DIR)
                .build()
                .intoFolder(OUTPUT_DIR);// 输出

    }

    public void outputJson() throws Exception {
        String url = URI;
        HttpClient httpClient = new HttpClient(url);
        System.out.println("开始请求:" + url);
        InputStream ipputStream = httpClient.execute();
        byte[] data = StreamTool.read(ipputStream);
        System.out.println("请求结果:" + new String(data, "UTF-8"));
        StreamTool.saveDataToFile(data, FILE_PATH);
        System.out.println("请求结果已成功保存到本地:" + FILE_PATH);

    }
}
