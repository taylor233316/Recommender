package com.bjut;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;


/**
 * @className: CodeGenerate
 * @Description: TODO
 * @version: v1.８.0
 * @author: GONGWENXUE
 * @date: 2023/7/23 17:41
 */
public class CodeGenerate {

    public static void main(String[] args) {
//        String url = "jdbc:mysql:///web?serverTimezone=GMT%2B8&characterEncoding=UTF-8&useUnicode=true&useSSL=false";
        String url = "jdbc:mysql:///web?serverTimezone=GMT%2B8&characterEncoding=UTF-8&useUnicode=true&useSSL=false";
        String username = "root";
        String password = "123456";
        String author = "chen";
        String outputDir = "F:\\1-project\\demo\\src\\main\\java";
        String basePackage = "com.bjut";
        String moduleName = "sys";
        String mapperLocation = "F:\\1-project\\demo\\src\\main\\resources\\mapper\\" + moduleName;
        String tables = "x_role_menu";
        String tablePrefix = "x_";
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(basePackage) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperLocation)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("x_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
