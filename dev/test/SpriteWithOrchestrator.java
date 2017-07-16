import b2fj.memory.Memory;

public class SpriteWithOrchestrator {

    // location of VIC
    private static final int VICII = 0xD000;
    
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
    private static SpriteWithOrchestrator[] _sprites = new SpriteWithOrchestrator[8];
    
    private int _mask;
    
    private SpriteWithOrchestrator (int idx) {
        _idx = idx;
        _mask = (1 << _idx);
        _color = Memory.peek(VICII+39+_idx);
        _x = ((Memory.peek(VICII+16) >> _idx) & 1) * 256 + Memory.peek(VICII+ _idx*2);
        _y = Memory.peek(VICII+1+ _idx*2);
        _enabled = ((Memory.peek(VICII+21) >> _idx) & 1) == 1;
        _multiColor = ((Memory.peek(VICII+28) >> _idx) & 1) == 1;
        _xStretch = ((Memory.peek(VICII+29) >> _idx) & 1) == 1;
        _yStretch = ((Memory.peek(VICII+23) >> _idx) & 1) == 1;
        
        // By default, the sprite definition is in the casssette buffer.
        // TODO: make this relocable.
        Memory.poke(2040+_idx, 13);
    }
    
     // Factory method.
    public static synchronized SpriteWithOrchestrator getSprite (int idx) {
        if (_sprites[idx] == null)
            _sprites[idx] = new SpriteWithOrchestrator(idx);
        return _sprites[idx];    
    }
    
    // Foreground color
    
    private int _color;
    public int getColor () {
        return _color;
    }
    public void setColor (int color) {
        _color = color;
        Memory.poke(VICII+39+_idx, _color);
    }
    
    // X position
    
    private int _x;
    public int getX () {
        return _x;
    }
    public void setX (int x) {
        _x = x;
        if (_x > 255)
            Memory.poke(VICII+16, Memory.peek(VICII+16) | _mask);
        else
            Memory.poke(VICII+16, Memory.peek(VICII+16) & (~_mask));
        Memory.poke(VICII+ _idx*2, _x & 0xff);
    }
    
    // Y position
    
    private int _y;
    public int getY () {
        return _y;
    }
    public void setY (int y) {
        _y = y;
        Memory.poke(VICII+1+ _idx*2, _y);
    }
    
    // On/Off
    
    private boolean _enabled;
    public boolean isEnabled () {
        return _enabled;
    }
    public void setEnabled (boolean enabled) {
        _enabled = enabled;
        if (_enabled)
            Memory.poke(VICII+21, Memory.peek(VICII+21) | _mask);
        else
            Memory.poke(VICII+21, Memory.peek(VICII+21) & (~_mask));
    }
    
    // Multicolor mode
    
    private boolean _multiColor;
    public boolean isMultiColor () {
        return _multiColor;
    }
    public void setMultiColor (boolean multiColor) {
        _multiColor = multiColor;
        if (_multiColor)
            Memory.poke(VICII+28, Memory.peek(VICII+28) | _mask);
        else
            Memory.poke(VICII+28, Memory.peek(VICII+28) & (~_mask));
    }
    
    // X stretch 
    
    private boolean _xStretch;
    public boolean isXStretch () {
        return _xStretch;
    }
    public void setXStretch (boolean xStretch) {
        _xStretch = xStretch;
        if (_xStretch)
            Memory.poke(VICII+29, Memory.peek(VICII+29) | _mask);
        else
            Memory.poke(VICII+29, Memory.peek(VICII+29) & (~_mask));
    }
    
    // Y stretch 
    
    private boolean _yStretch;
    public boolean isYStretch () {
        return _yStretch;
    }
    public void setYStretch (boolean yStretch) {
        _yStretch = yStretch;
        if (_yStretch)
            Memory.poke(VICII+23, Memory.peek(VICII+23) | _mask);
        else
            Memory.poke(VICII+23, Memory.peek(VICII+23) & (~_mask));
    }
    
    private static Thread _animator = new Thread() {
        public void run() {
            while (true) {
                for (int i = 0; i < _sprites.length; ++i) {
                	SpriteWithOrchestrator s = _sprites[i];
                    if (s == null) break;
                    s._y = s._y - 4 - (i % 4);
                    if (s._y < 7) {
                        s._y = 250;
                        s._x += 77 + i*7;
                        if (s._x > 320) s._x = s._x - 311 + i*3;
                        if ((++s._color) > 15)
                            s._color = 2;
                        s.setX(s._x);
                        //s.setColor(s._color);
                        Memory.poke(VICII+39+s._idx, s._color);
                    }
                    //setY(_y);
                    Memory.poke(VICII+1+ s._idx*2, s._y);
                }
            } // forever
        }
    };
    
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
    
    private native static void sayHello();
    
	public static void main(String[] args) {
    
        // Writes sprite image date into cassette buffer
        for (int i = 832; i<= 894; i++) {
            Memory.poke(i, _shape[i-832]);
        }
        
        // Set multicolors colors
        Memory.poke(VICII+37,BROWN);
        Memory.poke(VICII+38,WHITE);
        
        // Set border, scrren & text colors
        Memory.poke(VICII+32, WHITE);
        Memory.poke(VICII+33, BLACK);
        Memory.poke(VICII+34, BLUE);
        
        // Creates a baloon in the sky
        Sprite s = Sprite.getSprite(0);
        s.setColor(LIGHT_GREEN);
        s.setX(66);
        s.setY(250);
        s.setXStretch(false);
        s.setYStretch(true);
        s.setMultiColor(true);
        s.setEnabled(true);

        // Creates a baloon in the sky
        s = Sprite.getSprite(1);
        s.setColor(RED);
        s.setX(91);
        s.setY(250);
        s.setXStretch(false);
        s.setYStretch(true);
        s.setMultiColor(true);
        s.setEnabled(true);

        _animator.start();

        // print something
        String MSG = "one thread for this text...and one thread for the baloon...";
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
