
package com.ktn.craftsman.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * @描述： Activity字符串处理工具<br>
 */
public class StringUtil {
	/**
	 * 比较版本大小
	 */
	private static String[] sSrc_a;
	private static int iSrcLenght;
	private static String[] sDest_a;
	private static int iDestLenght;
	
	
	
	/**
	 * 版本号比较
	 * @param version_src
	 *            原版本号
	 * @param version_dest
	 *            更新的版本号
	 * @return result，返回1，版本号升级，返回0，版本号不升级
	 */
	public static int compareVersion(String version_src, String version_dest) {
		int result = 0;
		try {
			result = 0;
			if (version_src.contains(".")) {
				/**
				 * split方法对"."不起作用，所有要替换成其他字符，此处用"/"代替
				 */
				version_src = version_src.replace(".", "/");
				sSrc_a = version_src.split("/");
			}

			if (version_dest.contains(".")) {
				version_dest = version_dest.replace(".", "/");
				sDest_a = version_dest.split("/");
			}
			/**
			 * 下面分了4种情况进行判断
			 */
			if (sSrc_a == null && sDest_a == null) {
				/**
				 * 考虑version_src为空的情况
				 */
				if (version_src.equals("")) {
					result = 1;
				} else {
					if (StringUtil.parseInt(version_src) >= StringUtil
							.parseInt(version_dest)) {
						result = 0;
					} else {
						result = 1;
					}
				}
			} else if (sSrc_a == null && sDest_a != null) {
				if (version_src.equals("")) {
					result = 1;
				} else {
					if (Integer.parseInt(version_src) > Integer
							.parseInt(sDest_a[0])) {
						result = 0;
					} else if (Integer.parseInt(version_src) == Integer
							.parseInt(sDest_a[0])) {
						if (Integer.parseInt(sDest_a[1]) > 0) {
							result = 1;
						} else {
							result = 0;
						}
					} else {
						result = 1;
					}
				}
			} else if (sSrc_a != null && sDest_a == null) {
				if (Integer.parseInt(version_dest) > Integer
						.parseInt(sSrc_a[0])) {
					result = 1;
				} else {
					result = 0;
				}
			} else if (sSrc_a != null && sDest_a != null) {
				iSrcLenght = sSrc_a.length;
				iDestLenght = sDest_a.length;
				if (iSrcLenght >= iDestLenght) {
					for (int i = 0; i < iDestLenght; i++) {
						if (Integer.parseInt(sDest_a[i]) > Integer
								.parseInt(sSrc_a[i])) {
							result = 1;
							break;
						} else if (Integer.parseInt(sDest_a[i]) < Integer
								.parseInt(sSrc_a[i])) {
							result = 0;
							break;
						}
					}
				} else if (iSrcLenght < iDestLenght) {
					for (int j = 0; j < iSrcLenght; j++) {
						if (Integer.parseInt(sDest_a[j]) > Integer
								.parseInt(sSrc_a[j])) {
							result = 1;
							break;
						} else if (Integer.parseInt(sDest_a[j]) < Integer
								.parseInt(sSrc_a[j])) {
							result = 0;
							break;
						} else if (Integer.parseInt(sDest_a[j]) == Integer
								.parseInt(sSrc_a[j])) {
							result = -1;
						}
					}
					if (result == -1) {
						if (Integer.parseInt(sDest_a[iSrcLenght]) > 0) {
							result = 1;
						} else {
							result = 0;
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		return result;
	}
	
    /**
      * @method: subMaxString 
      * @Description: 获取最大长度字符创
      * @param srcStr
      * @param maxLen： 指定最大长度
      * @return
     */
    public static String subMaxString(String srcStr, int maxLen) {
        String ret = srcStr == null ? "" : srcStr;
        if (!TextUtils.isEmpty(ret) && maxLen > 0) {
            if (ret.length() > maxLen) {
                ret = ret.substring(0, maxLen) + "...";
            }
        }
        return ret;
    }
    
    /**
     * 倒序截取
     * @param srcStr
     * @param maxLen
     * @return
     */
    public static String subDescString(String srcStr,int maxLen){
    	String ret = srcStr == null ? "" : srcStr;
        
    	if (!TextUtils.isEmpty(ret) && maxLen > 0) {
            if (ret.length() > maxLen) {
                ret = ret.substring(srcStr.length()- maxLen, srcStr.length());
            }
        }
        return ret;
    }
    
    
    public static String filterBlankChar(String src){
    	if(TextUtils.isEmpty(src)){
    		return "";
    	}
    	src= src.trim();
    	if(TextUtils.isEmpty(src)){
    		return "";
    	}
    	
    	int length = src.length();
    	
    	String ret ="";
    	
    	for(int i = 0 ;i < length ; i++){
    		char c = src.charAt(i);
    		if(c != ' ') {
    			ret+=c;
    		}
    	}
    	return ret ;
    }
    
    
    
    public static String formatAppVersion(String version){
    	if(!TextUtils.isEmpty(version)) {
        	String s1[] = version.split("[.]");
       	     if(s1 != null){
       	       String result = "";
       	       boolean isFirstPosiont = true ;
       		   for(int i = 0 ; i< s1.length ;  i++ ) {
       			   if(isFirstPosiont && s1[i].equals("0")) {
       				   // 把前面的0 去掉
       			   }else {
       				 result += s1[i];
       				 isFirstPosiont  = false; 
       			   }
       		   }
       		   
       		   if(TextUtils.isEmpty(result)) {
       			   return "0";
       		   }else {
       			   return result;
       		   }
       	   }
    	}
    	 return "0";
    }
    
    
    /**
     * @method: subMaxString 
     * @Description: 获取最大长度字符创
     * @param srcStr 逆序截取
     * @param maxLen： 指定最大长度 
     * @return
    */
    public static String subReverseMaxString(String srcStr, int maxLen) {
        String ret = srcStr == null ? "" : srcStr;
        if (!TextUtils.isEmpty(ret) && maxLen > 0) {
            if (ret.length() > maxLen) {
                ret = ret.substring(srcStr.length() - maxLen, srcStr.length());
            }
        }
        return ret;
    }
    
    /*
     * 聊天文件中使用的文件名截取
     */
    public static String subContent(String content) {
		String endContent = "";
		String startContent = "";
		String resurnContent = "";
		if (content.length() > 12) {
			endContent = content.substring(content.length() - 7,
					content.length());
			startContent = content.substring(0, 5);
			resurnContent = startContent + "..." + endContent;
		}else{
			return content;
		}
		return resurnContent;
	}

	 /**
	  * 是否空字符串
	  * @param str
	  * @return
	  */
    public static  boolean isEmptyString(String str) {
    	if (TextUtils.isEmpty(str)) {
    		return true;
    	}
//    	if(str.equals("null")) {
//    		return true;
//    	}
    	if(str.equals("\"null\"")) {
    		return true;
    	}
    	return false;
    }
    
    //检测是否为空字符
    public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

    /**
     * long->String
     * 
     * @param number
     * @return
     */
    public static String longToString(long number) {
        return String.valueOf(number);
    }

    /**
      * @method: str2Hex 
      * @Description: 把16进制的字串转换为16进制的整数
      * @param strHex
      * @return
      * @throws
     */
    public static int str2Hex(String strHex){
        if(TextUtils.isEmpty(strHex)){
            return 0;
        }
        return Integer.parseInt(strHex.replaceAll("^0[x|X]", ""), 16);
    }
    
    /**
     * String->float
     */
    public static float stringToFloat(String str) {
        float ret = 0;
        try {
            ret = Float.valueOf(str);
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return ret;
    }

    /**
     * String->long
     */
    public static long stringToLong(String str) {
        long ret = 0;
        try {
            ret = Long.valueOf(str);
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return ret;
    }

    /*
     * 字符串转int, defRet : 转换失败使用的默认值
     */
    public static int string2Int(String strNum, int defRet) {
        int num = defRet;
        try {
            num = Integer.valueOf(strNum);
        } catch (Exception e) {
           // e.printStackTrace();
        }
        return num;
    }

    /**
     * @param s
     * @return
     */
    public static boolean isNotBlank(String s) {
        if (s != null && !s.trim().equals("")) {
            return true;
        } else
            return false;
    }
    
    /**
     * @param s 去掉空格
     * @return 
     */
    public static String removeBlank(String s) {
        if (s != null ) {
            return  s.replace(" ","");
        } else{
            return s;
        }
    }


    /**
     * long->M,K,B
     * 
     * @param size
     */
   /* public static String sizeToString(long size) {
        DecimalFormat df = new DecimalFormat("0.00");
        String mysize = "";
        if (size > 1024 * 1024) {
            mysize = df.format(size / 1024f / 1024f) + "M";
        } else if (size > 1024) {
            mysize = df.format(size / 1024f) + "K";
        } else {
            mysize = size + "B";
        }
        return mysize;
    	return formatFileSize(size);
    }*/
    
    /**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		if (fileS == 0) {
			return "0.00B";
		}
		DecimalFormat dFormat = new DecimalFormat("0.00");//#.00
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = dFormat.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = dFormat.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = dFormat.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = dFormat.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}


    
    /**
     * 把秒变成时分秒的字符串
     * 
     * @param size
     */
	public static String formatscendtodate(int second) {
		String mysecond = "";
		if (second > 3600) {
			if (second % 3600 > 60) {
				mysecond = (second / 3600) + "时" + second % 3600 / 60 + "分" + second % 3600 % 60 + "秒";
			} else {
				mysecond = (second / 3600) + "时" + second % 3600 + "秒";
			}

		} else if (second % 3600 == 0) {
			mysecond = second / 3600 + "时";
		} else {
			if (second > 60) {
				mysecond = second / 60 + "分" + second % 60 + "秒";
			} else {
				mysecond = second + "秒";
			}

		}
		return mysecond;
	}
    /**
     * long->String
     * 
     * @param time
     * @return
     */
    public static String timeToString(long time) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date(time);
        return format1.format(currentTime);
    }

    /**
     * long->Date
     * 
     * @param time
     * @return
     */
    public static Date timeToDate(long time) {
        Date currentTime = new Date(time);
        return currentTime;
    }

    public static String getTimesInterval(String time1, String time2) {
        return longToString(Long.valueOf(time2) - Long.valueOf(time1));
    }

    /**
     * <默认构造函数>
     */
    private StringUtil() {
    }

    public static String SOAP_RSP_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body>";
    public static String SOAP_RSP_BOTTOM = "</soapenv:Body></soapenv:Envelope>";
    public static String SOAP_REQ_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body>";
    public static String SOAP_REQ_BOTTOM = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
    public static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    public static String uperFirstString(String fieldName) {
        if (null != fieldName && fieldName.length() > 0) {
            fieldName = fieldName.substring(0, 1).toUpperCase()
                    + fieldName.substring(1, fieldName.length());
        }
        return fieldName;
    }

    public static String getObjectName(String packageName) {
        if (packageName.indexOf(".") > 0) {
            return packageName.substring(packageName.lastIndexOf(".") + 1);
        } else {
            return packageName;
        }
    }

    /**
     * 判断是否存在汉字
     * 
     * @param str 要判断的字符串
     */
    public static boolean isGB(String str) {
        char[] chars = str.toCharArray();
        boolean isGB2312 = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
                        && ints[1] <= 0xFE) {
                    isGB2312 = true;
                    break;
                }
            }
        }
        return isGB2312;
    }

    /**
     * 获取合法字符的长度
     * 
     * @param str 字符串
     */
    public static int getCount(String str) {
        int isLD = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLowerCase(ch)) {
                isLD++;
            } else if (Character.isUpperCase(ch)) {
                isLD++;
            } else if (Character.isDigit(ch)) {
                isLD++;
            } else if (String.valueOf(ch).equals("_")) {
                isLD++;
            } else if (String.valueOf(ch).equals("-")) {
                isLD++;
            } else if (String.valueOf(ch).equals("@")) {
                isLD++;
            } else if (String.valueOf(ch).equals(".")) {
                isLD++;
            } else {
                isLD = isLD + 0;
            }
        }
        return isLD;
    }

    /**
     * 判断是否存在非法字符. <br>
     * 如果输入的字符长度等于合法字符的长度 返回true输入正确<br>
     * 否则返回false输入含有非法字符
     * 
     * @param str 字符串
     */
    public static boolean isENOrDigit(String str) {
        return getCount(str) == str.length();
    }

    public static long parseLong(String value) {
    	if(TextUtils.isEmpty(value)){
    		return 0;
    	}
    	
    	int size = value.length();
    	String newValue ="";
    	for(int i = 0  ; i< size ; i++){
    		char c= value.charAt(i);
    		if((c >= '0' && c <= '9') || c =='.') {
    			newValue+=c;
    		}else {
    			break;
    		}
    	}
    	
    	if(TextUtils.isEmpty(newValue)){
    		return 0;
    	}
    	
        try {
            return Long.parseLong(newValue);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @method: parseLong
     * @Description: TODO
     * @param value
     * @return
     * @throws
     */
    public static long parseLong(int value) {
        try {
            return (long) value;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int innerWidth = 1020;
    public static int innerHeight = 640;

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static int parseInt(long value) {
        try {
            return Integer.parseInt(value+"");
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 将字符串转double
     * 
     * @param value
     * @return
     */
    public static double parseDouble(String value) {
    	if(TextUtils.isEmpty(value)){
    		return 0;
    	}
    	
    	int size = value.length();
    	String newValue ="";
    	for(int i = 0  ; i< size ; i++){
    		char c= value.charAt(i);
    		if((c >= '0' && c <= '9') || c =='.') {
    			newValue+=c;
    		}else {
    			break;
    		}
    	}
    	
    	if(TextUtils.isEmpty(newValue)){
    		return 0;
    	}
    	
        try {
            return Double.parseDouble(newValue);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isValidIdentity(String identity) {
        int len = identity.length();
        if (!(15 == len || 18 == len)) {
            return false;
        }
        char[] charAry = new char[15];

        for (int i = 0; i < charAry.length; i++) {
            charAry[i] = (char) identity.charAt(i);
            if (charAry[i] >= (char) 128) {
                return false;
            }
        }
        return true;
    }

   

    public static boolean parseBoolean(String value) {
        if (value.equals("1"))
            return true;
        else
            return false;
    }

    /**
     * 将SQL语句中不能正常使用的特殊字符转为可使用的形式。 目前实现转换的特殊字符为：“'”、“&”、“:”。
     * 
     * @param aConvert 要进行转换的字符串
     * @return 转换后的字符串
     */
    public static String strConvert(String aConvert) {
        if (null == aConvert || "".equals(aConvert)) {
            return aConvert;
        }
        aConvert = aConvert.replaceAll("'", "''");
        aConvert = aConvert.replaceAll("&", "'||'&'||'");
        // aConvert = aConvert.replaceAll(":", "'||':'||'");
        return aConvert;
    }

    

    /**
     * 截掉超长的字符:
     */
    public static String truncateStr(String str, int cnt) {
        String newStr = "";
        if (str.length() > cnt) {
            newStr = str.substring(0, cnt);
        } else {
            newStr = str;
        }
        int chineseCnt = getChineseNum(newStr);
        // // 可变步长调节字符长度到CNT（其中汉字在oracle中占3位字长）用来加快截取速度。
        while (chineseCnt * 2 + newStr.length() > cnt) {
            String temp = newStr.substring(0, newStr.length() - chineseCnt / 2);
            if (temp.length() + getChineseNum(temp) * 2 <= cnt) {
                break;
            }
            newStr = temp;
            chineseCnt = getChineseNum(newStr);
        }
        // 精确调整字符长度趋近于或等CNT
        for (;;) {
            if (chineseCnt * 2 + newStr.length() <= cnt)
                return newStr;
            newStr = newStr.substring(0, newStr.length() - 1);
            chineseCnt = getChineseNum(newStr);
        }
        // return newStr;
    }

    private static int getChineseNum(String str) {
        char[] charAry = new char[str.length()];
        int cnt = 0;
        for (int i = 0; i < charAry.length; i++) {
            charAry[i] = (char) str.charAt(i);
            if (charAry[i] >= (char) 128) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * 去除路径最后的"/"。将路径"./rms/"格式化成"./rms"
     * 
     * @return
     */
    public static String formatPath(String path) {
        if (path != null) {
            if (path.endsWith("/")) {
                return path.substring(0, path.length() - 1);
            } else {
                return path;
            }
        }
        return "";
    }

    public static String s2hex(String s) {
        String unicode = "";
        char[] charAry = new char[s.length()];
        for (int i = 0; i < charAry.length; i++) {
            charAry[i] = (char) s.charAt(i);
            if (charAry[i] >= (char) 128) {
                unicode += "\\u" + Integer.toString(charAry[i], 16);
            } else {
                unicode += "\\u00" + Integer.toString(charAry[i], 16);
            }
        }
        return unicode;
    }


    /*
     * 获取6位随即数
     */
    public static int getRandomCode() {
        Random r = new Random();
        int x = r.nextInt(9999);
        if (x > 100000) {
            return x;
        } else {
            return x;
        }

    }

    /**
     * added by chenwei record crash log field
     * 
     * @return
     */
    public static String getBuildString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n\r\n------Build------\r\n")
                .append("BOARD = " + Build.BOARD + "\r\n")
                .append("BRAND = " + Build.BRAND + "\r\n")
                .append("CPU_ABI = " + Build.CPU_ABI + "\r\n")
                .append("DEVICE = " + Build.DEVICE + "\r\n")
                .append("DISPLAY = " + Build.DISPLAY + "\r\n")
                .append("FINGERPRINT = " + Build.FINGERPRINT + "\r\n")
                .append("HOST = " + Build.HOST + "\r\n")
                .append("ID = " + Build.ID + "\r\n")
                .append("MANUFACTURER = " + Build.MANUFACTURER + "\r\n")
                .append("PRODUCT = " + Build.PRODUCT + "\r\n")
                .append("TAGS = " + Build.TAGS + "\r\n")
                .append("TYPE = " + Build.TYPE + "\r\n")
                .append("USER = " + Build.USER + "\r\n")
                .append("TIME = " + Build.TIME + "\r\n")
                .append("MODEL = " + Build.MODEL + "\r\n");

        sb.append("\r\n------Build.VERSION------\r\n")
                .append("CODENAME = " + Build.VERSION.CODENAME + "\r\n")
                .append("INCREMENTAL = " + Build.VERSION.INCREMENTAL + "\r\n")
                .append("RELEASE = " + Build.VERSION.RELEASE + "\r\n")
                .append("SDK = " + Build.VERSION.SDK + "\r\n")
                .append("SDK_INT = " + Build.VERSION.SDK_INT + "\r\n");
        return sb.toString();
    }

    public static String replaceUnderline(String src) {
        if (TextUtils.isEmpty(src)) {
            return "-";
        }

        return src.replaceAll("\\_", "-");
    }

    // 比较2个字符串
    public static boolean comparTwoString(String s1, String s2) {
        boolean ret = false;
        String s11 = s1 == null ? "" : s1;
        String s22 = s2 == null ? "" : s2;
        ret = s11.equals(s22);
        return ret;
    }

    // 根据路径取出文件名 path 是绝对路劲 type 是媒体类型 0 代码图片 1 代表音频

    public static String getFileNameByPath(String path, int type) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        String fileName = null;
        int index = path.lastIndexOf("/");
        if (index >= 0) {
            fileName = path.substring(index + 1);
        }

        if (!TextUtils.isEmpty(fileName)) {
            if (type == 0) {
                index = fileName.lastIndexOf(".");
                if (index < 0) {
                    return null;
                }

                String sufferx = fileName.substring(index);
                if (TextUtils.isEmpty(sufferx)) {
                    return null;
                }
                if (sufferx.equalsIgnoreCase(".jpg")
                        || sufferx.equalsIgnoreCase(".png")
                        || sufferx.equalsIgnoreCase(".jif")
                        || sufferx.equalsIgnoreCase(".jpeg")
                        || sufferx.equalsIgnoreCase(".bmp")) {
                    return fileName;
                } else {
                    return null;
                }
            } else if (type == 1) {
                index = fileName.lastIndexOf(".");
                if (index < 0) {
                    return null;
                }

                String sufferx = fileName.substring(index);
                if (TextUtils.isEmpty(sufferx)) {
                    return null;
                }
                if (sufferx.equalsIgnoreCase(".amr")) {
                    return fileName;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public static String getNumKb(String s) {
        // NumberFormat formatter = new DecimalFormat("###,##0.00");
        String ret = "";
        if (TextUtils.isEmpty(s)) {
            return ret;
        }

        try {
            if (s.contains(".")) {
                int index = s.indexOf(".");
                String point = s.substring(index, s.length());
                // 只包含小数点 没有小数的情况 自动补齐2个0
                if (point.length() == 1) {
                    point += "00";
                } else if (point.length() == 2) {
                    // 包含以为小数的情况
                    point += "0";
                }
                NumberFormat formatter = new DecimalFormat("###,##0");
                String integetStr = s.substring(0, index);
                ret = formatter.format(Double.valueOf(integetStr)) + "";
                return ret += point;
            } else {
                NumberFormat formatter = new DecimalFormat("###,##0");
                ret = formatter.format(Double.valueOf(s)) + "";
                return ret += ".00";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return ret;

        // try {
        // ret = formatter.format(Double.valueOf(s)) + "";
        // } catch (NumberFormatException e) {
        // e.printStackTrace();
        // }

        // return ret;
    }

    // 2014-10-10 zhuxiang add for bug0000027 start
    public static String getAccountFormatStr(String s) {
        StringBuffer ret = new StringBuffer();
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (i > 0 && i % 4 == 0) {
                    ret.append(' ');
                }
                ret.append(s.charAt(i));
            }
        }
        return ret.toString();
    }

    // 2014-10-10 zhuxiang add for bug0000027 end

    /**
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     * @param content
     * @return
     */
    private static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

//    public static void lengthFilter(final Context context, EditText editText,
//            final int maxLength) {
//        if (editText != null) {
//            InputFilter[] filters = new InputFilter[1];
//            filters[0] = new InputFilter.LengthFilter(maxLength) {
//                @Override
//                public CharSequence filter(CharSequence source, int start,
//                        int end, Spanned dest, int dstart, int dend) {
//                    int destLen = StringUtil.getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)
//                    int sourceLen = StringUtil.getCharacterNum(source
//                            .toString());
//                    if (destLen + sourceLen > maxLength) {
//                        VDialogAdapter.getDialogInstance().toast(
//                                AppAdapter.getInstance().getContext(),
//                                AppAdapter.getInstance().getContext().getResources().getString(
//                                		CPResourceUtil.getStringId("reach_max_input_len")));
//                        return "";
//                    }
//                    return source;
//                }
//            };
//            editText.setFilters(filters);
//        }
//    }

    public static String toAddHorizontalline(String s) {
        String ret = "";
        StringBuffer buffer = new StringBuffer();
        if (s != null) {
            if (s.length() == 11) {
                String s1 = s.substring(0, 3);
                String s2 = s.substring(3, 7);
                String s3 = s.substring(7, 11);
                buffer.append(s1).append("-").append(s2).append("-").append(s3);
                ret = buffer.toString();
            } else {
                ret = s;
            }
        }
        return ret;
    }

    /*
     * format long number to string : 00000000
     */
    public static String formatBillNumber2String(long num) {
        final int STRING_LENGHT = 8;
        final String TAG = "0";
        String strNum = String.valueOf(num);
        strNum = TextUtils.isEmpty(strNum) ? "" : strNum;
        strNum = strNum.trim();
        if (strNum.length() > STRING_LENGHT) {
            strNum = strNum.substring(strNum.length() - STRING_LENGHT);
        }
        int subLen = STRING_LENGHT - strNum.length();
        for (int i = subLen; i > 0; --i) {
            strNum = (TAG + strNum);
        }
        return strNum;
    }

    /*
     * format long number to string : 32位
     */
    public static String formatPayAccount2String(long num) {
        final int STRING_LENGHT = 32;
        final String TAG = "0";
        String strNum = String.valueOf(num);
        strNum = TextUtils.isEmpty(strNum) ? "" : strNum;
        strNum = strNum.trim();
        if (strNum.length() > STRING_LENGHT) {
            strNum = strNum.substring(strNum.length() - STRING_LENGHT);
        }
        int subLen = STRING_LENGHT - strNum.length();
        for (int i = subLen; i > 0; --i) {
            strNum = (TAG + strNum);
        }
        return strNum;
    }

   

    /*
     * 格式化金额字符串
     */
    public static String getFormatAmountNumber(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return "00.00";
        }
        return "";
    }

    /*
     * 获取表单时间戳，与IOS、PC统一，获取S级时间戳（10位整数） 注意：所有申请审批表单，都要从这里获取时间戳，不然其他平台无法正确解析
     */
    public static long getApplyTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /*
     * 两个数字字符串相加 主要是超大的2个数字串 用其他类型的相加会溢出或者被格式化
     */
    public static String addtwoString(String add1, String add2) {
        // System.out.println(add1);
        // System.out.println(add2);
        if (TextUtils.isEmpty(add1)) {
            return add2;
        }
        if (TextUtils.isEmpty(add2)) {
            return add1;
        }
        String addNum1 = (new StringBuffer(add1)).reverse().toString();
        String addNum2 = (new StringBuffer(add2)).reverse().toString();
        int length1 = addNum1.length();
        int length2 = addNum2.length();
        // tempAdd1的位数多余tempAdd2
        String tempAdd1 = addNum1;
        String tempAdd2 = addNum2;
        if (length1 < length2) {
            tempAdd1 = addNum2;
            tempAdd2 = addNum1;
        }
        StringBuffer sb = new StringBuffer();
        int jinWei = 0;
        int tailNum = 0;
        for (int i = 0; i < tempAdd2.length(); i++) {
            int temp1 = Character.digit(tempAdd2.charAt(i), 10);
            int temp2 = Character.digit(tempAdd1.charAt(i), 10);
            int total = temp1 + temp2 + jinWei;
            if (total > 9) {
                jinWei = 1;
                tailNum = Character.digit(String.valueOf(total).charAt(1), 10);
            } else {
                jinWei = 0;
                tailNum = total;
            }
            sb.append(tailNum);
        }
        for (int i = tempAdd2.length(); i < tempAdd1.length(); i++) {
            int temp = Character.digit(tempAdd1.charAt(i), 10);
            int total = temp + jinWei;
            if (total > 9) {
                jinWei = 1;
                tailNum = 0;
                sb.append(tailNum);
            } else {
                jinWei = 0;
                sb.append(total);
                System.out.println(i + 1);
                sb.append(tempAdd1.substring(i + 1));
                break;
            }
        }
        if (jinWei > 0) {
            sb.append(jinWei);
        }
        // System.out.println(sb.reverse().toString());
        return sb.reverse().toString();
    }

	/*
     * 是否双击事件
     */
	private static long lastClickTime = 0;
	public static boolean isDoubleClick() {
		long time = System.currentTimeMillis();
		long diff = time - lastClickTime;
		lastClickTime = time;
		if (diff > 0 && diff < 400) {
			return true;
		}
		return false;
	}
    

    /**
     * 转换文件size为 “B”
     */
    public static long parseSizeToB(String size) {
    	long bSize = 0;
    	if (TextUtils.isEmpty(size)) {
    		return bSize;
    	}
    	// K、 KB、 M、 MB
    	double dSize = parseDouble(size);
    	String upSize = size.toUpperCase();
    	double realSize = dSize;
    	if (upSize.contains("K")) {
    		realSize = dSize * 1024;
    	} else if (upSize.contains("M")) {
    		realSize = dSize * 1024 * 1024;
    	}
    	return (long)realSize;
    }

	/**
	 * 格式化会议与会人的状态，将所有的与会人状态串成一个格式化的字符串：uid:status,uid:status,uid:status, 然后返回uaid -- status的map集合
	 * @param statusString
	 * @return
	 */
	public static Map<Long, Integer> formatMeetingMemberStatus(final String statusString) {
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		if (!TextUtils.isEmpty(statusString)) {
			// 成员状态的列表
			String[] msList = statusString.split(",");
			if (msList != null) {
				// 成员状态
				for (String ms: msList) {
					// ms 应该是这样的： uaid:status, 如 327:1
					String[] sta = ms.split(":");
					if (sta != null && sta.length == 2) {
						Long uId = StringUtil.parseLong(sta[0]);
						int st = StringUtil.parseInt(sta[1]);
						if (uId > 0) {
							map.put(uId, st);
						}
					}
				}
			}
		}

		return map;
	}


	/**
	 * 用以计时操作的相关方法
	 * @param num
	 * @return
	 */
	public static String format(int num){
		
		String s = num + "";
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}
	
	
	   //输出随机的字母和数字
    public static String getStringRandom(int length) {  
        String NUM = "num";
        String CHAR = "char";
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum  = "";
//            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; 
            if(i==0) {
                charOrNum = CHAR;
            } else {
                charOrNum = NUM;
            }
            //输出字母还是数字  
            if( CHAR.equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
//              int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                int temp1 = 97;  //只生成小写字母
                val += (char)(random.nextInt(26) + temp1);  
            } else if( NUM.equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
    
    public static String handleLongUserName(String username,int showLength) {
        if (username.length() >8 && showLength < username.length()-1) {
            username = username.substring(0, showLength)+"...";
        }
        return username;
    }
}
