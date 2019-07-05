package com.hw.congfial;

import com.google.auto.service.AutoService;
import com.hw.annotation.NeedsPermissin;
import com.hw.annotation.OnNeverAskAgain;
import com.hw.annotation.OnPermissinDenied;
import com.hw.annotation.OnShowRationale;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

/**
 * 注解处理器
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-16.
 */
@AutoService(Processor.class)
public class ParmissionProexssor extends AbstractProcessor {

    private static final String TAG = ParmissionProexssor.class.getName();

    /**
     * 报告错误，警告，提示
     */
    private Messager messager;

    /**
     * 包含很多Elements工具
     */
    private Elements elementsUtils;

    /**
     * 用来创建按新的class文件
     */
    private Filer filer;

    /**
     * 包含用于操作TypesMirror工具方法
     */
    private Types typesUtils;

    /**
     * 做初始化工作
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typesUtils = processingEnv.getTypeUtils();
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    /**
     * 获取支持注解的类型
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(NeedsPermissin.class.getCanonicalName());
        types.add(OnPermissinDenied.class.getCanonicalName());
        types.add(OnNeverAskAgain.class.getCanonicalName());
        types.add(OnShowRationale.class.getCanonicalName());
        // return super.getSupportedAnnotationTypes();
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //返回最新的支持的JDK版本
        return SourceVersion.latest();
    }

    /**
     * 注解处理器的核心方法，处理具体的注解实现，生成Java文件
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取在activity中有NeedsPermissin注解的方法,拿到的结果是一个集合
        Set<? extends Element> needsPermissinSet = roundEnv.getElementsAnnotatedWith(NeedsPermissin.class);
        //保存起来，键值对：key--com.***MainActivity，value--所有带有NeedsPermissin注解的方法
        Map<String, List<ExecutableElement>> needsPermissionMap = new HashMap<>();
        //遍历
        for (Element element : needsPermissinSet) {
            //转成方法元素（结构体元素）
            ExecutableElement executableElement = (ExecutableElement) element;
            //通过方法元素获取所属的MainActivity类名,如：com.hw.premission.MainActivity
            String activityName = getActivityName(executableElement);
            //从缓存集合中获取MainActivity带有NeedsPermissin注解的方法集合
            List<ExecutableElement> list = needsPermissionMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                //先加入map集合，引用变量list可以动态改变值
                needsPermissionMap.put(activityName, list);
            }
            //将带有NeedsPermissin注解的方法加入list集合
            list.add(executableElement);
        }

        //OnNeverAskAgain注解
        Set<? extends Element> onNeverAskAgainSet = roundEnv.getElementsAnnotatedWith(OnNeverAskAgain.class);
        Map<String, List<ExecutableElement>> onNeverAskAgainMap = new HashMap<>();
        for (Element element : onNeverAskAgainSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onNeverAskAgainMap.computeIfAbsent(activityName, k -> new ArrayList<>());
            // if (list == null) {
            //     list = new ArrayList<>();
            //     onNeverAskAgainMap.put(activityName, list);
            // }
            list.add(executableElement);
        }

        //OnPermissinDenied注解
        Set<? extends Element> onPermissinDeniedSet = roundEnv.getElementsAnnotatedWith(OnPermissinDenied.class);
        Map<String, List<ExecutableElement>> onPermissinDeniedSetMap = new HashMap<>();
        for (Element element : onPermissinDeniedSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onPermissinDeniedSetMap
                    .computeIfAbsent(activityName, k -> new ArrayList<>());
            // if (list == null) {
            //     list = new ArrayList<>();
            //     onPermissinDeniedSetMap.put(activityName, list);
            // }
            list.add(executableElement);
        }


        //OnShowRationale注解
        Set<? extends Element> onShowRationaleSet = roundEnv.getElementsAnnotatedWith(OnShowRationale.class);
        Map<String, List<ExecutableElement>> onShowRationaleSetMap = new HashMap<>();
        for (Element element : onShowRationaleSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onShowRationaleSetMap.computeIfAbsent(activityName, k -> new ArrayList<>());
            // if (list == null) {
            //     list = new ArrayList<>();
            //     onShowRationaleSetMap.put(activityName, list);
            // }
            list.add(executableElement);
        }

        for (String activityName : needsPermissionMap.keySet()) {
            List<ExecutableElement> needsPermissionElements = needsPermissionMap.get(activityName);
            List<ExecutableElement> onNeverAskAgainElements = onNeverAskAgainMap.get(activityName);
            List<ExecutableElement> onPermissinDeniedElements = onPermissinDeniedSetMap.get(activityName);
            List<ExecutableElement> onShowRationaleElements = onShowRationaleSetMap.get(activityName);

            final String CLASS_SUFFIX = "$Permissions";
            Filer filer = processingEnv.getFiler();
            //创建一个新的源文件（class），并且返回一个对象以允许写入它
            try {
                // JavaFileObject javaFileObject = filer.createClassFile(activityName + CLASS_SUFFIX);
                JavaFileObject javaFileObject = filer.createClassFile(activityName + CLASS_SUFFIX);
                String packageName = getPackageName(needsPermissionElements.get(0));
                //定义writer对象
                Writer writer = javaFileObject.openWriter();
                String activitySimpleName =
                        needsPermissionElements.get(0).getEnclosingElement().getSimpleName().toString() + CLASS_SUFFIX;
                //生成包package
                writer.write("package " + packageName + ";\n");
                //生成要导入的接口类（必须手动导入）
                writer.write("import com.hw.library.RequestPermission;\n");
                writer.write("import com.hw.library.PermissionRequest;\n");
                writer.write("import com.hw.library.PermissionUtils;\n");
                writer.write("import android.support.v7.app.AppCompatActivity;\n");
                writer.write("import android.support.v4.app.ActivityCompat;\n");
                writer.write("import android.support.annotation.NonNull;\n");
                writer.write("import java.lang.ref.WeakReference;\n");
                //生成类implements
                // public class MainActivity$Permissions implements RequestPermission<RequestPermissionActivity> {
                writer.write("public class " + activitySimpleName + " implements RequestPermission<" + activityName
                        + ">{\n");
                writer.write("private static final int REQUEST_SHOWCAMERA = 666;\n");
                writer.write("private static String[] PERMISSION_SHOWCAMERA;\n");

                // // writer.write("public MainActivity$Permissions() {\n");
                // writer.write("public " + activitySimpleName + "() {\n ");
                // writer.write("}");
                // writer.write("\n");
                // writer.write("\n");
                // writer.write("\n");
                // // @Override
                // // public void requestPermission (RequestPermissionActivity target, String[]permissions){
                // //     PERMISSION_SHOWCAMERA = permissions;
                // //     if (PermissionUtils.hasSelfParmissions(target, PERMISSION_SHOWCAMERA)) {
                // //         target.showCamera();
                // //     } else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {
                // //         target.showRationaleForCamera(new PermissionRequestIml(target));
                // //     } else {
                // //         ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
                // //     }
                // // }
                // writer.write("@Override\n");
                // writer.write("public void requestPermission (RequestPermissionActivity target, String[]permissions){\n");
                // writer.write("PERMISSION_SHOWCAMERA = permissions;\n");
                // writer.write("if (PermissionUtils.hasSelfParmissions(target, PERMISSION_SHOWCAMERA)) {\n");
                // writer.write("target.showCamera();\n");
                // writer.write("}");
                // writer.write(" else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {\n");
                // writer.write("target.showRationaleForCamera(new PermissionRequestIml(target));\n");
                // writer.write("}");
                // writer.write(" else {\n");
                // writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n");
                // writer.write("}");
                // // @Override
                // // public void onRequestPermissionResult(RequestPermissionActivity target, int requestCode,
                // // @NonNull int[] grantResults) {
                // //     switch (requestCode) {
                // //         case REQUEST_SHOWCAMERA:
                // //             if (PermissionUtils.verifyParmissions(grantResults)) {
                // //                 target.showCamera();
                // //             } else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {
                // //                 target.showNeverAskForCamera();
                // //             } else {
                // //                 target.showDeniedForCamera();
                // //             }
                // //             break;
                // //         default:
                // //             break;
                // //     }
                // // }
                // writer.write("@Override\n");
                // writer.write("public void onRequestPermissionResult(RequestPermissionActivity target, int requestCode,@NonNull int[] grantResults) {\n");
                // writer.write("switch (requestCode) {\n");
                // writer.write("case REQUEST_SHOWCAMERA:\n");
                // writer.write("if (PermissionUtils.verifyParmissions(grantResults)) {\n");
                // writer.write("target.showCamera();\n");
                // writer.write("} else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {\n");
                // writer.write(" target.showNeverAskForCamera();\n");
                // writer.write("} else {\n");
                // writer.write("target.showDeniedForCamera();\n");
                // writer.write("}");
                // writer.write("break;");
                // writer.write("default:\n");
                // writer.write("break;\n");
                // writer.write("}\n");
                // writer.write("}");
                //
                // // private static final class PermissionRequestIml implements PermissionRequest {
                // //
                // //     private final WeakReference<RequestPermissionActivity> weakTarget;
                // //
                // //     private PermissionRequestIml(RequestPermissionActivity target) {
                // //         this.weakTarget = new WeakReference(target);
                // //     }
                // //
                // //     @Override
                // //     public void proceed() {
                // //         RequestPermissionActivity target = this.weakTarget.get();
                // //         if (target != null) {
                // //             ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
                // //         }
                // //     }
                // // }
                // writer.write("\n");
                // writer.write("\n");
                //
                // writer.write("private static final class PermissionRequestIml implements PermissionRequest {\n");
                // writer.write("private final WeakReference<RequestPermissionActivity> weakTarget;\n\n");
                // writer.write("private PermissionRequestIml(RequestPermissionActivity target) {\n");
                // writer.write("this.weakTarget = new WeakReference(target);\n");
                // writer.write("}");
                // writer.write("\n");
                // writer.write("@Override\n");
                // writer.write("public void proceed() {\n");
                // writer.write("RequestPermissionActivity target = this.weakTarget.get();\n");
                // writer.write("if (target != null) {\n");
                // writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n");
                // writer.write("}\n");
                // writer.write("}\n");
                // writer.write("}");

                writer.write("\n");
                writer.write("\n");
                writer.write("\n");

                writer.write("\n}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private String getActivityName(ExecutableElement executableElement) {
        String packageName = getPackageName(executableElement);
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //com.hw.premission.MainActivity
        return packageName + "." + typeElement.getSimpleName().toString();

    }

    private String getPackageName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        return packageName;
    }
}
