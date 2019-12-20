package com.demo.netty.handler;


import com.demo.netty.util.OTUCodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class MockDeviceCodec extends ByteToMessageCodec<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes();
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            String msg = OTUCodecUtil.Byte2StringSerialize(in);
            out.add(msg);
        } finally {
            resetBuffer(ctx,in);
        }
    }


    /**
     * 移动指针到开始位置
     *
     * @param ctx
     * @param in
     */
    private void resetBuffer(ChannelHandlerContext ctx, ByteBuf in) {
        int left = in.readableBytes();
        int start = in.readerIndex();
        if (left > 0 && in.readerIndex() > 0) {
            for (int index = 0; index < left; index++) {
                in.setByte(index, in.getByte(index + start));
            }
            in.setIndex(0, left);
        }
    }
}
