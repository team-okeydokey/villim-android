package net.villim.villim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class LineView extends View {

    private Paint paint;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public LineView(Context context, int x1, int y1, int x2, int y2, int color) {
        super(context);
        paint = new Paint();
        paint.setColor(color);

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

}
