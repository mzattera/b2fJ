import b2fj.memory.*;

import java.io.*;

public class Sprite extends Thread {

    private static final int NUM_SPRITES = 4;

    // Address of VIC Chip
    private static final int VIC_II = 0xD000;

    private static final int SPRITE_X = VIC_II + 0;
    private static final int SPRITE_X_MSB = VIC_II + 16;
    private static final int SPRITE_Y = VIC_II + 1;
    private static final int SPRITE_ENABLE = VIC_II + 21;
    private static final int SPRITE_EXPAND_Y = VIC_II + 23;
    private static final int SPRITE_EXPAND_X = VIC_II + 29;
    private static final int MULTICOLOR_SPRITE_SELECT = VIC_II + 28;
    private static final int BORDER_COLOR = VIC_II + 32;
    private static final int BACKGROUND_COLOR_0 = VIC_II + 33;
    private static final int BACKGROUND_COLOR_1 = VIC_II + 34;
    private static final int BACKGROUND_COLOR_2 = VIC_II + 35;
    private static final int BACKGROUND_COLOR_3 = VIC_II + 36;
    private static final int SPRITE_MULTICOLOR_0 = VIC_II + 37;
    private static final int SPRITE_MULTICOLOR_1 = VIC_II + 38;
    private static final int SPRITE_COLOR = VIC_II + 39;

    private static final int BLACK = 0;
    private static final int WHITE = 1;
    private static final int RED = 2;
    private static final int CYAN = 3;
    private static final int PURPLE = 4;
    private static final int BLUE = 6;
    private static final int YELLOW = 7;
    private static final int ORANGE = 8;
    private static final int BROWN = 9;
    private static final int PINK = 10;
    private static final int DARK_GREY = 11;
    private static final int GREY = 12;
    private static final int LIGHT_GREEN = 13;
    private static final int LIGHT_BLUE = 14;
    private static final int LIGHT_GRAY = 15;

    // Links this Sprite to a physical C64 sprite.
    private int _idx = -1;

    // The 8 sprites; each one has its index.
    private static Sprite[] _sprites = new Sprite[8];

    private int _mask;

    private Sprite (int idx) {
        _idx = idx;
        _mask = (1 << _idx);
        _color = Memory.peek(SPRITE_COLOR+_idx);
        _x = ((Memory.peek(SPRITE_X_MSB) >> _idx) & 1) * 256 + Memory.peek(VIC_II+ _idx*2);
        _y = Memory.peek(SPRITE_Y+ _idx*2);
        _enabled = ((Memory.peek(SPRITE_ENABLE) >> _idx) & 1) == 1;
        _multiColor = ((Memory.peek(MULTICOLOR_SPRITE_SELECT) >> _idx) & 1) == 1;
        _xStretch = ((Memory.peek(SPRITE_EXPAND_X) >> _idx) & 1) == 1;
        _yStretch = ((Memory.peek(SPRITE_EXPAND_Y) >> _idx) & 1) == 1;

        // By default, the sprite definition is in the casssette buffer.
        // TODO: make this relocable.
        Memory.poke(2040+_idx, 13);
    }

     // Factory method.
    public static synchronized Sprite getSprite (int idx) {
        if (_sprites[idx] == null)
            _sprites[idx] = new Sprite(idx);
        return _sprites[idx];
    }

    // Foreground color

    private int _color;
    public int getColor () {
        return _color;
    }
    public void setColor (int color) {
        _color = color;
        Memory.poke(SPRITE_COLOR+_idx, _color);
    }

    // X position

    private int _x;
    public int getX () {
        return _x;
    }
    public void setX (int x) {
        _x = x;
        if (_x > 255)
            Memory.poke(SPRITE_X_MSB, Memory.peek(SPRITE_X_MSB) | _mask);
        else
            Memory.poke(SPRITE_X_MSB, Memory.peek(SPRITE_X_MSB) & (~_mask));
        Memory.poke(VIC_II+ _idx*2, _x & 0xff);
    }

    // Y position

    private int _y;
    public int getY () {
        return _y;
    }
    public void setY (int y) {
        _y = y;
        Memory.poke(SPRITE_Y+ _idx*2, _y);
    }

    // On/Off

    private boolean _enabled;
    public boolean isEnabled () {
        return _enabled;
    }
    public void setEnabled (boolean enabled) {
        _enabled = enabled;
        if (_enabled)
            Memory.poke(SPRITE_ENABLE, Memory.peek(SPRITE_ENABLE) | _mask);
        else
            Memory.poke(SPRITE_ENABLE, Memory.peek(SPRITE_ENABLE) & (~_mask));
    }

    // Multicolor mode

    private boolean _multiColor;
    public boolean isMultiColor () {
        return _multiColor;
    }
    public void setMultiColor (boolean multiColor) {
        _multiColor = multiColor;
        if (_multiColor)
            Memory.poke(MULTICOLOR_SPRITE_SELECT, Memory.peek(MULTICOLOR_SPRITE_SELECT) | _mask);
        else
            Memory.poke(MULTICOLOR_SPRITE_SELECT, Memory.peek(MULTICOLOR_SPRITE_SELECT) & (~_mask));
    }

    // X stretch

    private boolean _xStretch;
    public boolean isXStretch () {
        return _xStretch;
    }
    public void setXStretch (boolean xStretch) {
        _xStretch = xStretch;
        if (_xStretch)
            Memory.poke(SPRITE_EXPAND_X, Memory.peek(SPRITE_EXPAND_X) | _mask);
        else
            Memory.poke(SPRITE_EXPAND_X, Memory.peek(SPRITE_EXPAND_X) & (~_mask));
    }

    // Y stretch

    private boolean _yStretch;
    public boolean isYStretch () {
        return _yStretch;
    }
    public void setYStretch (boolean yStretch) {
        _yStretch = yStretch;
        if (_yStretch)
            Memory.poke(SPRITE_EXPAND_Y, Memory.peek(SPRITE_EXPAND_Y) | _mask);
        else
            Memory.poke(SPRITE_EXPAND_Y, Memory.peek(SPRITE_EXPAND_Y) & (~_mask));
    }

    // Moves the Sprite
    public void run() {
        while (true) {
            _y = _y - (_idx % 4) - 1;
            if (_y < 7) {
                _y = 250;
                _x += 77 + _idx*7;
                if (_x > 320) _x = (_x % 100) + 24;
                if ((++_color) > 15)
                    _color = 2;
                setX(_x);
                //setColor(_color);
                Memory.poke(SPRITE_COLOR+_idx, _color);
            }
            //setY(_y);
            Memory.poke(SPRITE_Y+ _idx*2, _y);
        } // forever
    }

    // Shape of the sprite (baloon with C= & J)
    private static final int[] _shape = {
        0x02,0xAA,0x00,
        0x0A,0xAA,0x80,
        0x2A,0xAA,0xA0,
        0xAA,0xFA,0xA8,
        0xAB,0xEF,0xA8,
        0xAB,0xAE,0xA8,
        0xAB,0xEF,0xA8,
        0xAA,0xFA,0xA8,
        0x2A,0xAA,0xA0,
        0x3A,0xBA,0xB0,
        0x32,0xBA,0x30,
        0x30,0x30,0x30,
        0x0C,0x30,0xC0,
        0x0C,0x30,0xC0,
        0x01,0x55,0x00,
        0x01,0x5D,0x00,
        0x01,0x5D,0x00,
        0x01,0xDD,0x00,
        0x01,0xDD,0x00,
        0x01,0x75,0x00,
        0x00,0x54,0x00
    };

	public static void main(String[] args) {

        // Writes sprite image date into cassette buffer
        for (int i = 832; i<= 894; i++) {
            Memory.poke(i, _shape[i-832]);
        }

        // Set multicolors colors
        Memory.poke(SPRITE_MULTICOLOR_0,BROWN);
        Memory.poke(SPRITE_MULTICOLOR_1,WHITE);

        // Set border, screen & text colors
        Memory.poke(BORDER_COLOR, WHITE);
        Memory.poke(BACKGROUND_COLOR_0, BLACK);
        Memory.poke(BACKGROUND_COLOR_1, BLUE);

        // Creates a baloon in the sky
        for (int i=0; i<NUM_SPRITES; ++i) {
            Sprite s = Sprite.getSprite(i);
            s.setColor(2 + i);
            s.setX(((66 * i) % 200) + 55);
            s.setY(250);
            s.setXStretch(false);
            s.setYStretch(true);
            s.setMultiColor(true);
            s.setEnabled(true);
            s.start();
        }

        // print something
        String MSG = "one thread for this text... and one thread for each baloon...";
        while (true) {
            System.out.print(MSG);
            try {
                Thread.sleep(666);
            }
            catch (InterruptedException w) {
                return;
            }
        }
    }
}
