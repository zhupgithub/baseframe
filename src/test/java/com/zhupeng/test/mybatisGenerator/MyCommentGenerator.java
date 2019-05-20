package com.zhupeng.test.mybatisGenerator;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
/**
 * mybatis generator 自定义comment生成器.
 * @ApiModel(value = "返回响应类型")
 * @author yihui
 *
 */
public class MyCommentGenerator extends DefaultCommentGenerator {// implements
																	// CommentGenerator
																	// {
 
	private Properties properties;
	private Properties systemPro;
	private boolean suppressDate;
	private boolean suppressAllComments;  //是否不显示所有的注解
	private String currentDateStr;
 
	public MyCommentGenerator() {
		super();
		properties = new Properties();
		systemPro = System.getProperties();
		suppressDate = false;
		suppressAllComments = false;
		currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
	}
	
	@Override
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
 
		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
 
		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
	}
	
	
	
	
	@Override
	protected String getDateString() {
		String result = null;
		if (!suppressDate) {
			result = currentDateStr;
		}
		return result;
	}
	

	

	

	
	
	
	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
 
		StringBuilder sb = new StringBuilder();
 
		innerEnum.addJavaDocLine("/**");
		// addJavadocTag(innerEnum, false);
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString());
		innerEnum.addJavaDocLine(" */");
	}
	
	/**
	 * 添加属性上的注解、注释
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
 
		StringBuilder sb = new StringBuilder();
 
		field.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedColumn.getRemarks());
		field.addJavaDocLine(sb.toString());
 
		// addJavadocTag(field, false);
 
		field.addJavaDocLine(" */");
		//(required = false , dataType ="Integer" ,name ="status" ,notes ="0：初始状态，1:支付成功2支付失败"  ,value = "0：初始状态，1:支付成功2支付失败", example = "1")
		
		
		field.addAnnotation("@ApiModelProperty(value = \""+introspectedColumn.getRemarks()+"\" "
				+ ", name = \""+introspectedColumn.getJavaProperty()+"\" , example = \"" +( introspectedColumn.getDefaultValue() == null ?"" : introspectedColumn.getDefaultValue()) +"\" , required = "+
				!introspectedColumn.isNullable()
				+" , dataType = \""+introspectedColumn.getFullyQualifiedJavaType().getShortName()+"\" ,notes = \""+introspectedColumn.getRemarks()+"\""
				+")");
		
		// field.addAnnotation("@Size(min = 0, max = " +
		// introspectedColumn.getLength() + " , message =
		// \"长度必须在{min}和{max}之间\")");
		// field.addAnnotation("@NotNull"); if
		// (introspectedColumn.isStringColumn()) {
		// topLevelClass.addImportedType("javax.validation.constraints.Size");
		// field.addAnnotation("@Size(min = 0, max = " +
		// introspectedColumn.getLength() + " , message =
		// \"长度必须在{min}和{max}之间\")"); }
		
		
		
		//FIXME  朱朋注释   start
//		List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
//		for (IntrospectedColumn col : primaryKeyColumns) {
//			if (col.getActualColumnName().equals(introspectedColumn.getActualColumnName())) {
//				field.addAnnotation("@Id");
//			}
//		}
//		field.addAnnotation("@Column(name = \"" + introspectedColumn.getActualColumnName() + "\")");
		//FIXME  朱朋注释   end
		
	}
	

	
	/**
	 * get方法上加上注解
	 */
	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
 
		method.addJavaDocLine("/**");
 
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(introspectedColumn.getRemarks());
		method.addJavaDocLine(sb.toString());
 
		sb.setLength(0);
		sb.append(" * @return ");
		sb.append(introspectedColumn.getActualColumnName());
		sb.append(" ");
		sb.append(introspectedColumn.getRemarks());
		method.addJavaDocLine(sb.toString());
 
		// addJavadocTag(method, false);
 
		method.addJavaDocLine(" */");
	}
	
	/**
	 * set方法上加上注解
	 */
	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
 
		method.addJavaDocLine("/**");
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(introspectedColumn.getRemarks());
		method.addJavaDocLine(sb.toString());
 
		Parameter parm = method.getParameters().get(0);
		sb.setLength(0);
		sb.append(" * @param ");
		sb.append(parm.getName());
		sb.append(" ");
		sb.append(introspectedColumn.getRemarks());
		method.addJavaDocLine(sb.toString());
 
		// addJavadocTag(method, false);
 
		method.addJavaDocLine(" */");
	}
	
	/**
	 * 实体类中的导包、类上加注释、文件上加上注解
	 */
	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		//TODO  导入包
		//import io.swagger.annotations.ApiModelProperty;  io.swagger.annotations.ApiModel;
		topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
		topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
		if (suppressAllComments) {
			return;
		}
 
		StringBuilder sb = new StringBuilder();
 
		topLevelClass.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append(introspectedTable.getRemarks());
//		topLevelClass.addJavaDocLine(sb.toString());
 
//		sb.setLength(0);
		sb.append("\r\n * @author ");
		sb.append("朱朋");
		sb.append(" \r\n ");
		sb.append("* @date ");
		sb.append(currentDateStr);
		topLevelClass.addJavaDocLine(sb.toString());
		// addJavadocTag(innerClass, markAsDoNotDelete);
 
		topLevelClass.addJavaDocLine(" */");
		//添加类上的注释
		topLevelClass.addAnnotation("@ApiModel(value = \""+introspectedTable.getRemarks()+"\")");
		
		//FIXME  朱朋  start
//		topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTable() + "\")");
//		topLevelClass.addAnnotation("@Alias(\""
//				+ toLowerCaseFirstOne(introspectedTable.getTableConfiguration().getDomainObjectName()) + "\")");
		//FIXME  朱朋  end
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	//FIXME  朱朋  start
//	@Override
//	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
//		
//		if (suppressAllComments) {
//            return;
//        }
// 
//        StringBuilder sb = new StringBuilder();
// 
//        innerClass.addJavaDocLine("/**");
//        sb.append(" * ");
//        sb.append(introspectedTable.getFullyQualifiedTable());
//        innerClass.addJavaDocLine(sb.toString());
// 
//        sb.setLength(0);
//        sb.append(" * @author ");
//        sb.append(systemPro.getProperty("user.name"));
//        sb.append(" ");
//        sb.append(currentDateStr);
// 
//        //      addJavadocTag(innerClass, markAsDoNotDelete);
// 
//        innerClass.addJavaDocLine(" */");
//		
//	}
//	//FIXME  朱朋  end
	
//	//FIXME  朱朋注释   start
//	@Override
//	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
//		if (suppressAllComments) {
//			return;
//		}
// 
//		StringBuilder sb = new StringBuilder();
// 
//		field.addJavaDocLine("/**");
//		sb.append(" * ");
//		sb.append(introspectedTable.getFullyQualifiedTable());
//		field.addJavaDocLine(sb.toString());
//		field.addJavaDocLine(" */");
//	}
//	//FIXME  朱朋注释   end
	
//	//FIXME  朱朋注释   start
//	@Override
//	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
//			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
//		System.out.println(field.getName());
//		
//		//FIXME  朱朋注释   start
////		TODO  可以考虑加入swagger注解    field.addAnnotation("@Column(name = \"" + field.getName() + "\")");
//		//FIXME  朱朋注释   end
//	}
//	//FIXME  朱朋注释   end
	
	
//	//FIXME  朱朋注释   start	
//	@Override
//	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
//		if (suppressAllComments) {
//			return;
//		}
//		StringBuilder sb = new StringBuilder();
//		innerClass.addJavaDocLine("/**");
//		sb.append(" * ");
//		sb.append("---类上注释----");
//		sb.append(introspectedTable.getFullyQualifiedTable());
//		sb.append(" ");
//		sb.append(getDateString());
//		System.out.println("类上注释："+sb.toString());
//		innerClass.addJavaDocLine(sb.toString());
//		innerClass.addJavaDocLine(" */");
//	}
//	//FIXME  朱朋注释   end
	
//	//FIXME  朱朋注释   start
//	@Override
//	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
//		javaElement.addJavaDocLine(" *");
//		StringBuilder sb = new StringBuilder();
//		sb.append(" * ");
//		sb.append(MergeConstants.NEW_ELEMENT_TAG);
//		if (markAsDoNotDelete) {
//			sb.append(" do_not_delete_during_merge");
//		}
//		String s = getDateString();
//		if (s != null) {
//			sb.append(' ');
//			sb.append(s);
//		}
//		javaElement.addJavaDocLine(sb.toString());
//	}	
//	//FIXME  朱朋注释   end
	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	
	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
	}
	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		return;
	}
 
	@Override
	public void addComment(XmlElement xmlElement) {
		return;
	}
	
	@Override
	public void addRootComment(XmlElement rootElement) {
		return;
	}

	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
	}
	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
	}
	@Override
	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
	}
 
	
	@Override
	public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
	}
	
	
	
	public static void main(String[] args) {
//		args = new String[] { "-configfile", "src\\test\\resources\\mybatis-generator.xml", "-overwrite" };
		args = new String[] { "-configfile", "src\\test\\java\\com\\zhupeng\\test\\mybatisGenerator\\mybatis-generator.xml", "-overwrite" };
		ShellRunner.main(args);
	}
}