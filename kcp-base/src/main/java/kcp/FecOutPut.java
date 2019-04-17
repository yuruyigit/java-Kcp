package kcp;

import com.backblaze.erasure.fec.FecEncode;
import io.netty.buffer.ByteBuf;

/**
 * Created by JinMiao
 * 2018/7/27.
 */
public class FecOutPut implements  KcpOutput{

    private KcpOutput output;

    private FecEncode fecEncode;


    protected FecOutPut(KcpOutput output, FecEncode fecEncode) {
        this.output = output;
        this.fecEncode = fecEncode;
    }

    @Override
    public void out(ByteBuf msg, Kcp kcp) {
        ByteBuf[] byteBufs = fecEncode.encode(msg);
        //out之后会自动释放你内存
        output.out(msg,kcp);
        if(byteBufs==null)
            return;
        for (int i = 0; i < byteBufs.length; i++) {
            ByteBuf parityByteBuf = byteBufs[i];
            output.out(parityByteBuf,kcp);
        }
    }
}
