package com.example.myandroid.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.example.myandroid.xml.model.Book;

public class SAXBookParser implements BookParser {

	@Override
	public List<Book> parser(InputStream input) throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser parser = saxParserFactory.newSAXParser();
		BookHandler mHandler = new BookHandler();
		parser.parse(input, mHandler);
		return mHandler.getBooks();
	}

	@Override
	public String serialize(List<Book> books) throws Exception {
		/**
		 * 取得SAXTransformerFactory实例
		 */
		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory
				.newInstance();
		/**
		 * 从factory获取TransformerHandler实例
		 */
		TransformerHandler handler = factory.newTransformerHandler();
		/**
		 * 从handler获取Transformer实例
		 */
		Transformer transformer = handler.getTransformer();
		/**
		 * 设置输出采用的编码方式
		 */
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		/**
		 * 是否自动添加额外的空白
		 */
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		/**
		 * 是否忽略XML声明
		 */
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		handler.setResult(result);

		String uri = ""; // 代表命名空间的URI 当URI无值时 须置为空字符串
		String localName = ""; // 命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串

		AttributesImpl attrs = new AttributesImpl(); // 负责存放元素的属性信息
		char[] ch = null;

		handler.startDocument();
		handler.startElement(uri, localName, "books", null);

		for (Book book : books) {
			attrs.clear(); // 清空属性列表
			// attrs.addAttribute(uri, localName, "id", "string",
			// String.valueOf(book.getId()));// 添加一个名为id的属性(type影响不大,这里设为string)
			// handler.startElement(uri, localName, "book", attrs); //
			// 开始一个book元素
			// // 关联上面设定的id属性

			/**
			 * 开始一个book元素
			 */
			handler.startElement(uri, localName, Book.ELEMENT, null);
			/**
			 * 开始一个id元素
			 */
			handler.startElement(uri, localName, Book.ELEMENT_ID, null);
			ch = String.valueOf(book.getId()).toCharArray();
			/**
			 * 设置id元素的文本节点
			 */
			handler.characters(ch, 0, ch.length);
			/**
			 * 结束id元素
			 */
			handler.endElement(uri, localName, Book.ELEMENT_ID);
			/**
			 * 开始一个name元素
			 */
			handler.startElement(uri, localName, Book.ELEMENT_NAME, null);
			ch = String.valueOf(book.getName()).toCharArray();
			/**
			 * 设置name元素的文本节点
			 */
			handler.characters(ch, 0, ch.length);
			/**
			 * 结束name元素
			 */
			handler.endElement(uri, localName, Book.ELEMENT_NAME);
			/**
			 * 开始一个price元素
			 */
			handler.startElement(uri, localName, Book.ELEMENT_PRICE, null);
			ch = String.valueOf(book.getPrice()).toCharArray();
			/**
			 * 设置price元素的文本节点
			 */
			handler.characters(ch, 0, ch.length);
			/**
			 * 结束price元素
			 */
			handler.endElement(uri, localName, Book.ELEMENT_PRICE);
			/**
			 * 结束一个book元素
			 */
			handler.endElement(uri, localName, Book.ELEMENT);
		}

		handler.endElement(uri, localName, "books");
		handler.endDocument();

		return writer.toString();
	}

	private class BookHandler extends DefaultHandler {

		private List<Book> books;
		private Book tmp;
		private StringBuilder stringBuilder;

		/**
		 * 返回解析后得到的Book对象集合
		 * 
		 * @return 解析后得到的Book对象集合
		 */
		public List<Book> getBooks() {
			return books;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			books = new ArrayList<Book>();
			stringBuilder = new StringBuilder();
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals(Book.ELEMENT)) {
				tmp = new Book();
			}
			stringBuilder.setLength(0);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals(Book.ELEMENT)) {
				books.add(tmp);
			} else if (localName.equals(Book.ELEMENT_ID)) {
				tmp.setId(Integer.parseInt(stringBuilder.toString()));
			} else if (localName.equals(Book.ELEMENT_NAME)) {
				tmp.setName(stringBuilder.toString());
			} else if (localName.equals(Book.ELEMENT_PRICE)) {
				tmp.setPrice(Float.parseFloat(stringBuilder.toString()));
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			stringBuilder.append(ch, start, length);
		}

	}

}
