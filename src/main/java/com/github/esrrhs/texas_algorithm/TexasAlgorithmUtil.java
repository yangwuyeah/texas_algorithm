package com.github.esrrhs.texas_algorithm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TexasAlgorithmUtil
{
	public static class KeyData
	{
		public KeyData(int index, int postion, long max, int type)
		{
			this.index = index;
			this.postion = postion;
			this.max = max;
			this.type = type;
		}

		private int index;
		private int postion;
		private long max;
		private int type;

		public int getIndex()
		{
			return index;
		}

		public int getPostion()
		{
			return postion;
		}

		public long getMax()
		{
			return max;
		}

		public int getType()
		{
			return type;
		}
	}

	public static ConcurrentHashMap<Long, KeyData> colorMap = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Long, KeyData> normalMap = new ConcurrentHashMap<>();

	public static void main(String[] args)
	{
		gen();
		genOpt();
		genTrans();
	}

	private static void gen()
	{
		File file = new File("texas_data.txt");
		if (!file.exists())
		{
			GenUtil.genKey();
			GenUtil.outputData();
		}
	}

	private static void genOpt()
	{
		File file = new File("texas_data.txt");
		if (file.exists())
		{
			GenOptUtil.optNormalData();
			GenOptUtil.optColorData();
		}
	}

	private static void genTrans()
	{
		File file = new File("texas_data.txt");
		if (file.exists())
		{
			for (int i = 6; i >= 2; i--)
			{
				GenTransUtil.N = i;
				GenTransUtil.genKey();
				GenTransUtil.transData();
			}
		}
	}

	public static void load()
	{
		try
		{
			FileInputStream inputStream = new FileInputStream("texas_data_color.txt");
			loadColor(inputStream);
			FileInputStream inputStream1 = new FileInputStream("texas_data_normal.txt");
			loadNormal(inputStream1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void loadNormal(InputStream inputStream) throws Exception
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		normalMap.clear();
		String str = null;
		while ((str = bufferedReader.readLine()) != null)
		{
			String[] params = str.split(" ");
			long key = Long.parseLong(params[0]);
			int i = Integer.parseInt(params[1]);
			int index = Integer.parseInt(params[2]);
			long max = Long.parseLong(params[5]);
			int type = Integer.parseInt(params[7]);

			KeyData keyData = new KeyData(index, i, max, type);
			normalMap.put(key, keyData);
		}
		bufferedReader.close();
	}

	public static void loadColor(InputStream inputStream) throws Exception
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		colorMap.clear();
		String str = null;
		while ((str = bufferedReader.readLine()) != null)
		{
			String[] params = str.split(" ");
			long key = Long.parseLong(params[0]);
			int i = Integer.parseInt(params[1]);
			int index = Integer.parseInt(params[2]);
			long max = Long.parseLong(params[5]);
			int type = Integer.parseInt(params[7]);

			KeyData keyData = new KeyData(index, i, max, type);
			colorMap.put(key, keyData);
		}
		bufferedReader.close();
	}

	public static byte strToPokeValue(String str)
	{
		if (str.equals("A"))
		{
			return Poke.PokeValue_A;
		}
		else if (str.equals("K"))
		{
			return Poke.PokeValue_K;
		}
		else if (str.equals("Q"))
		{
			return Poke.PokeValue_Q;
		}
		else if (str.equals("J"))
		{
			return Poke.PokeValue_J;
		}
		else
		{
			return Byte.parseByte(str);
		}
	}

	public static byte strToPoke(String str)
	{
		if (str.startsWith("方"))
		{
			return (new Poke(Poke.PokeColor_FANG, strToPokeValue(str.substring(1)))).toByte();
		}
		else if (str.startsWith("梅"))
		{
			return (new Poke(Poke.PokeColor_MEI, strToPokeValue(str.substring(1)))).toByte();
		}
		else if (str.startsWith("红"))
		{
			return (new Poke(Poke.PokeColor_HONG, strToPokeValue(str.substring(1)))).toByte();
		}
		else if (str.startsWith("黑"))
		{
			return (new Poke(Poke.PokeColor_HEI, strToPokeValue(str.substring(1)))).toByte();
		}
		else
		{
			return 0;
		}
	}

	public static String keyToStr(long key)
	{
		return GenUtil.toString(key);
	}

	public static List<Byte> strToPokes(String str)
	{
		List<Byte> ret = new ArrayList<>();
		String[] strs = str.split(",");
		for (String s : strs)
		{
			ret.add(strToPoke(s));
		}
		return ret;
	}

	public static KeyData getKeyData(String str)
	{
		List<Byte> pokes = strToPokes(str);
		if (pokes.size() != 7)
		{
			return null;
		}

		return getKeyData(pokes);
	}

	public static KeyData getKeyData(long key)
	{
		long colorKey = GenOptUtil.changeColor(key);
		KeyData color = colorMap.get(colorKey);
		long normalKey = GenOptUtil.removeColor(key);
		KeyData normal = normalMap.get(normalKey);
		if (color == null)
		{
			return normal;
		}
		if (normal == null)
		{
			return normal;
		}
		if (color.getIndex() > normal.getIndex())
		{
			return color;
		}
		else
		{
			return color;
		}
	}

	public static KeyData getKeyData(List<Byte> pokes)
	{
		long key = GenUtil.genCardBind(pokes);

		return getKeyData(key);
	}

	public static int getWinPosition(String str)
	{
		KeyData keyData = getKeyData(str);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getPostion();
	}

	public static int getWinPosition(List<Byte> pokes)
	{
		KeyData keyData = getKeyData(pokes);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getPostion();
	}

	public static double getWinProbability(String str)
	{
		KeyData keyData = getKeyData(str);
		if (keyData == null)
		{
			return 0;
		}
		return (double) keyData.getIndex() / GenUtil.total;
	}

	public static double getWinProbability(List<Byte> pokes)
	{
		KeyData keyData = getKeyData(pokes);
		if (keyData == null)
		{
			return 0;
		}
		return (double) keyData.getIndex() / GenUtil.total;
	}

	public static long getWinMax(String str)
	{
		KeyData keyData = getKeyData(str);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getMax();
	}

	public static long getWinMax(List<Byte> pokes)
	{
		KeyData keyData = getKeyData(pokes);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getMax();
	}

	public static int getWinType(String str)
	{
		KeyData keyData = getKeyData(str);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getType();
	}

	public static int getWinType(List<Byte> pokes)
	{
		KeyData keyData = getKeyData(pokes);
		if (keyData == null)
		{
			return 0;
		}
		return keyData.getType();
	}
}
