/* 
 * Copyright 2017 Robert Hewitt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.runouw.rcodec;

/**
 *
 * @author Robert
 */
public class JSONUtils {
    public static String cleanString(String str){
        StringBuilder newString = new StringBuilder();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            switch(c){
                case '\\':
                    newString.append('\\');
                    newString.append(c);
                    break;
                case '"':
                    newString.append('\\');
                    newString.append(c);
                    break;
                case '\n':
                    newString.append('\\');
                    newString.append('n');
                    break;
                case '\t':
                    newString.append('\\');
                    newString.append('t');
                    break;
                case '\r':
                    newString.append('\\');
                    newString.append('r');
                    break;
                default:
                    newString.append(c);
                    break;
            }
        }
        return newString.toString();
    }
    public static String restoreString(String str){
        StringBuilder newString = new StringBuilder();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            char next = ((i + 1) >= str.length()) ? '\0' : (str.charAt(i + 1));
            switch(c){
                case '\n':
                case '\r':
                case '\t':
                    break;
                case '\\':
                    switch(next){
                        case 't':
                            newString.append('\t');
                            i++;
                            break;
                        case 'n':
                            newString.append('\n');
                            i++;
                            break;
                        case 'r':
                            newString.append('\r');
                            i++;
                            break;
                        default:
                            newString.append(next);
                            i++;
                            break;
                    }
                    
                    break;
                default:
                    newString.append(c);
                    break;
            }
        }
        return newString.toString();
    }
}
