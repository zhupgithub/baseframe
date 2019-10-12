//public class base64 {
// 
//          
//
//    public static void main(String[] args) {
//    File file = new File("D:\\base64.txt");
//    String result = getFromBase64(file2String(file));
//    System.out.println(result);
//  }
// 
//          //文件转字符串
//          
//
//    public static String file2String(File file) {
//    try {
//      BufferedReader buffer = new BufferedReader(new FileReader(file));
//      StringBuilder sb = new StringBuilder();
//      String temp;
//      while ((temp = buffer.readLine()) != null) {
//        sb.append(temp);
// 
//      }
//      return sb.toString();
//    } catch (FileNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//      return null;
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//      return null;
//    }
//  }
//  //加密
//          
//
//    public static String getBase64(String str) {
//    byte[] b = null;
//    String s = null;
//    try {
//      b = str.getBytes("utf-8");
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
//    if (b != null) {
//       s = new BASE64Encoder().encode(b);
//    }
//    return s;
// 
//  }
// 
//          //解密
//          
//
//    public static String getFromBase64(String str) {
//    byte[] b = null;
//    String result = null;
//    if (str != null) {
//       BASE64Decoder decoder = new BASE64Decoder();
//       try {
//          b = decoder.decodeBuffer(str);
//          result = new String(b, "utf-8");
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//    }
//    return result;
//  }
// 
//}