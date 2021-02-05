package java.io;

public class BufferedReader extends Reader {
	private static final int DEFAULT_BUFFERSIZE = 64;
	private static final int MIN_BUFFERSIZE = 1;

	private final Reader reader;
	private final char[] buffer;
	private boolean skipLF;
	private int offset;
	private int limit;

	public BufferedReader(Reader reader) {
		this(reader, DEFAULT_BUFFERSIZE);
	}

	public BufferedReader(Reader reader, int size) {		
		super(reader);

		// The below is a temporary patch for  https://github.com/mzattera/b2fJ/issues/8
		lock = reader;

		if (size < MIN_BUFFERSIZE)
			size = MIN_BUFFERSIZE;

		this.reader = reader;
		this.buffer = new char[size];
	}

	@Override
	public void close() throws IOException {
		synchronized (lock) {
			this.limit = -1;
			this.reader.close();
		}
	}

	private void checkOpen() throws IOException {
		synchronized (lock) {
			if (this.limit < 0)
				throw new IOException("stream is closed");
		}
	}

	private boolean fillBufferAndSkipLF() throws IOException {
		synchronized (lock) {
			do {
				if (this.offset >= this.limit) {
					int len = 0;
					// some Reader may be return 0, which is bad behavior actually
					while (len == 0) {
						len = reader.read(this.buffer, 0, this.buffer.length);
					}
					if (len < 0)
						return false;

					this.offset = 0;
					this.limit = len;
				}

				if (skipLF) {
					skipLF = false;
					if (this.buffer[this.offset] == '\n') {
						this.offset++;
					}
				}
			} while (this.offset >= this.limit);

			return true;
		}
	}

	@Override
	public int read() throws IOException {
		synchronized (lock) {
			checkOpen();

			if (!fillBufferAndSkipLF())
				return -1;

			return this.buffer[this.offset++];
		}
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		synchronized (lock) {
			checkOpen();

			if (len == 0)
				return 0;

			if (len > this.buffer.length && !skipLF && this.offset >= this.limit)
				return this.reader.read(cbuf, off, len);

			if (!fillBufferAndSkipLF())
				return -1;

			int blen = this.limit - this.offset;
			if (blen > len)
				blen = len;

			System.arraycopy(this.buffer, this.offset, cbuf, off, blen);
			this.offset += blen;
			return blen;
		}
	}

	@Override
	public boolean ready() throws IOException {
		synchronized (lock) {
			checkOpen();

			return this.offset < this.limit || this.reader.ready();
		}
	}

	public String readLine() throws IOException {
		synchronized (lock) {
			int c = read();
			if (c < 0)
				return null;

			StringBuilder sb = new StringBuilder();
			while (c != '\n' && c != '\r') {
				sb.append((char) c);
				c = read();

				if (c < 0)
					break;
			}

			skipLF = (c == '\r');
			return sb.toString();
		}
	}
}