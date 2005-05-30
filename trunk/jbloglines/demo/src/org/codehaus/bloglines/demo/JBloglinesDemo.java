/*
 * Created on 29-May-2005
 *
 *
 * Copyright (c) Zohar Melamed & William Jones - All rights reserved.
 *
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Due credit should be given to The Codehaus and Contributors
 * http://timtam.codehaus.org/
 *
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *
 */
package org.codehaus.bloglines.demo;

import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;

import org.codehaus.bloglines.Bloglines;
import org.codehaus.bloglines.Outline;
import org.codehaus.bloglines.exceptions.BloglinesException;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class JBloglinesDemo extends JFrame {

	public class EntryWrapper {

		private final SyndEntry entry;

		public EntryWrapper(SyndEntry entry) {
			this.entry = entry;
		}
		public String toString() {
			return entry.getTitle();
		}
		/**
		 * @return Returns the entry.
		 */
		public SyndEntry getEntry() {
			return entry;
		}
	}

	public class FeedWrapper {

		private final Outline subscription;
		private final String name;

		public FeedWrapper(String name, Outline subscription) {
			this.name = name;
			this.subscription = subscription;
		}

		
		/**
		 * @return Returns the subscription.
		 */
		public Outline getSubscription() {
			return subscription;
		}


		public String toString() {
			return name +"("+subscription.getUnread()+")";
		}
	}

		
	private static final int COL_COUNT = 6;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList itemsList;

	private JEditorPane itemDetail;

	private JList subscriptionsList;

	private Bloglines bloglines = new Bloglines();

	private JPasswordField password;

	private JTextField userName;

	/**
	 * @throws HeadlessException
	 */
	public JBloglinesDemo() throws HeadlessException {
		super("JBloglines Demo");
	}

	private void createComponents() {
		// jgoodies l&f for that just out of the shower look
		try {
			UIManager.put("ClassLoader", LookUtils.class.getClassLoader());
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormLayout formLayout = new FormLayout(
				"left:max(40dlu;pref), 3dlu, 100dlu:grow,3dlu,70dlu,pref:grow",
				"pref,pref,pref,pref,pref,pref,pref,fill:pref:grow");

		DefaultFormBuilder builder = new DefaultFormBuilder(formLayout);

		builder.setDefaultDialogBorder();

		builder.appendSeparator("Account Details");
		userName = new JTextField("zohar.melamed@uk.bnpparibas.com");
		builder.append("User Name", userName);

		JButton connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					bloglines.setCredentials(userName.getText(),new String(password.getPassword()));
					Outline subscriptions  = bloglines.listSubscriptions();
					((DefaultListModel)subscriptionsList.getModel()).clear();
					showSubscriptions("",subscriptions);
				} catch (BloglinesException e1) {
					e1.printStackTrace();
				}finally{
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		builder.add(connect);
		builder.nextLine();

		password = new JPasswordField("iamzohar");
		password.setEchoChar('*');
		builder.append("Password", password);
		builder.nextLine();

		builder.appendSeparator("Subscriptions");
		subscriptionsList = new JList(new DefaultListModel());
		subscriptionsList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false) {
					FeedWrapper feed = (FeedWrapper) subscriptionsList.getSelectedValue();
					((DefaultListModel)itemsList.getModel()).clear();
					showFeedItems(feed);
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(subscriptionsList);
		builder.append(scrollPane, COL_COUNT);

		builder.appendSeparator("Items");
		itemsList = new JList(new DefaultListModel());
		itemsList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (e.getValueIsAdjusting() == false ) {
					itemDetail.setText("");
					EntryWrapper wrapper = (EntryWrapper) itemsList.getSelectedValue();
					if(wrapper!=null){
						SyndEntry entry = wrapper.getEntry();
						SyndContent description = entry.getDescription();
						if(description != null){
							
							itemDetail.setText("<html>"+description.getValue()+"</html>");
						}
					}
				}
			}
		});
		scrollPane = new JScrollPane(itemsList);
		builder.append(scrollPane, COL_COUNT);

		itemDetail = new JEditorPane("text/html" ,"");
		itemDetail.setEditable(false);
		scrollPane = new JScrollPane(itemDetail);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		builder.append(scrollPane, COL_COUNT);

		getContentPane().add(builder.getPanel());
	}

	
	private void showFeedItems(FeedWrapper feed) {
		Outline subscription = feed.getSubscription();
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			SyndFeed items = bloglines.getItems(subscription,false,null);
			List entries = items.getEntries();
			DefaultListModel model = (DefaultListModel) itemsList.getModel();
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				SyndEntry entry = (SyndEntry) iter.next();
				model.addElement(new EntryWrapper(entry));
			}
		} catch (BloglinesException e) {
			e.printStackTrace();
		}finally{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private void showSubscriptions(String parent,Outline subscription) {
		if(subscription.isFeed()){
			DefaultListModel model =  (DefaultListModel) subscriptionsList.getModel();
			model.addElement(new FeedWrapper(parent+"::"+subscription.getTitle(),subscription));
		}else{
			Outline[] children = subscription.getChildren();
			for (int i = 0; i < children.length; i++) {
				showSubscriptions(parent +" > "+subscription.getTitle(),children[i]);
			}			
		}
	}

	public static void main(String[] args) {
		JBloglinesDemo demo = new JBloglinesDemo();
		demo.createComponents();
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setSize(600, 660);
		demo.setVisible(true);
	}

}
