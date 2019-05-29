package com.zhupeng.baseframe.entity.aop;

import com.zhupeng.baseframe.utils.Stream2ByteArrayUtil;

import java.io.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 *
 *
 * <b>类名称：</b>RequestWrapper<br/>
 * <b>类描述：</b>复制request中的bufferedReader中的值<br/>
 * <b>修改备注：</b>参考：https://www.cnblogs.com/xincunyiren/p/7248034.html<br/>
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper{
    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = Stream2ByteArrayUtil.getByteByStream(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


}
