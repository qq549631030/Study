package com.example.myandroid.xml;

import java.io.InputStream;
import java.util.List;

import com.example.myandroid.xml.model.Book;

public interface BookParser {
	/**
	 * 解析输入流 得到Book对象集合
	 * 
	 * @param is
	 *            xml文件输入流
	 * @return　Book对象集合
	 * @throws Exception
	 */
	public List<Book> parser(InputStream input) throws Exception;

	/**
	 * 序列化Book对象集合 得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public String serialize(List<Book> books) throws Exception;
}
