package server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// Create a temporary ByteBuf and append to it all inbound bytes until we get the required amount of bytes.
public class ProcessingHandler
        extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        // Let's assume that the server receives the request and returns the intValue multiplied by 2.
        responseData.setIntValue(requestData.getIntValue() * 2);
//        responseData.setStringValue(requestData.getStringValue().toUpperCase());
        responseData.setFloatValue(requestData.getFloatValue());
        System.out.println("sent back response from server");
        ChannelFuture future = ctx.writeAndFlush(responseData);
        //future.addListener(ChannelFutureListener.CLOSE);
        //ctx.write(requestData);
    }
}