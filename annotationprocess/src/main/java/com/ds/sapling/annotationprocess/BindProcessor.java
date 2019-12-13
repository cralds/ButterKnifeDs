package com.ds.sapling.annotationprocess;

import com.ds.sapling.annotation.BindOnClick;
import com.ds.sapling.annotation.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
public class BindProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(BindView.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("==========start=======");
        if (set == null || set.size() == 0){
            return false;
        }

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> clickElements = roundEnvironment.getElementsAnnotatedWith(BindOnClick.class);
        System.out.println("=============element length=="+ elements.size() + "=======click==="+clickElements.size());
        Element mElement = elements.iterator().next();
        TypeMirror typeMirror = mElement.getEnclosingElement().asType();//getEnclosingElement获取该元素的父元素
        TypeName typeName = TypeName.get(typeMirror);
        //构造方法
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addParameter(typeName,"target")
                .addStatement("dealView(target, target.getWindow().getDecorView())")
                .addModifiers(Modifier.PUBLIC);

        ClassName view = ClassName.get("android.view","View");

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("dealView")
                .addParameter(typeName,"target",Modifier.FINAL)
                .addParameter(view ,"source")
                .addModifiers(Modifier.PUBLIC);

        //遍历所有的findViewById
        for (Element element : elements){
            System.out.println("==========element name======="+element.getSimpleName());

            Name elementName = element.getSimpleName();
            int id = element.getAnnotation(BindView.class).value();
            methodBuild.addStatement("target.$L = source.findViewById($L)",elementName.toString(),id);
        }

        //遍历所有的onclick方法
        for (Element element : clickElements){
            BindOnClick bindOnClick = element.getAnnotation(BindOnClick.class);
            int id = bindOnClick.value();
            System.out.println("==========element name click======="+element.getSimpleName());
            methodBuild.addCode("source.findViewById($L).setOnClickListener(new View.OnClickListener() {\n@Override\n",id);
            methodBuild.addCode("public void onClick(View v) {\n");
            methodBuild.addStatement("  target.$L()",element.getSimpleName());
            methodBuild.addStatement("}})");
        }

        //解绑方法
        MethodSpec unbind = MethodSpec.methodBuilder("unBind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        ClassName className = ClassName.get("com.ds.sapling.butterknife",mElement.getEnclosingElement().getSimpleName() + "_viewbinding");
        System.out.println("==========classname name======="+className);
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get("com.ds.sapling.buttermanager","UnBinder"))
                .addMethod(unbind)
                .addMethod(builder.build())
                .addMethod(methodBuild.build())
                .addModifiers(Modifier.PUBLIC)
                .build();


        JavaFile javaFile = JavaFile.builder(className.packageName(),typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
