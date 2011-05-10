/*
 *      Copyright 2009-2011 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sagealert.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.code.sagetvaddons.sagealert.client.ClientListenerSubscriptionForm.MediaType;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.Client.EventType;
import com.google.code.sagetvaddons.sagealert.shared.ClientService;
import com.google.code.sagetvaddons.sagealert.shared.ClientServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A GUI widget for modifying client alias and notification settings
 * @author dbattams
 * @version $Id$
 */
final class ClientSettingsPanel extends ContentPanel {
	static private final ClientSettingsPanel INSTANCE = new ClientSettingsPanel();
	static final ClientSettingsPanel getInstance() { return INSTANCE; }

	private ClientServiceAsync rpc;
	private RpcProxy<List<Client>> proxy;
	private BeanModelReader reader;
	ListLoader<ListLoadResult<Client>> loader;
	private ListStore<BeanModel> store;
	private List<ColumnConfig> cols;
	private Grid<BeanModel> grid;	

	private GridCellRenderer<BeanModel> listRenderer;

	/**
	 * Constructor
	 */
	private ClientSettingsPanel() {
		setHeading("Client Configuration");
		setLayout(new FitLayout());

		listRenderer = new GridCellRenderer<BeanModel>() {

			public Object render(final BeanModel model, String property, final ColumnData config, int rowIndex, int colIndex, ListStore<BeanModel> store, Grid<BeanModel> grid) {
				ComboBox<BaseModel> list = new ComboBox<BaseModel>();
				ListStore<BaseModel> data = new ListStore<BaseModel>();
				BaseModel bm = new BaseModel();
				bm.set("event", "notifyOnTvStart");
				bm.set("name", "Starts TV");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnTvStop");
				bm.set("name", "Stops TV");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnTvPause");
				bm.set("name", "Pauses TV");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnTvResume");
				bm.set("name", "Resumes TV");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnDvdStart");
				bm.set("name", "Starts BR/DVD");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnDvdStop");
				bm.set("name", "Stops BR/DVD");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnDvdPause");
				bm.set("name", "Pauses BR/DVD");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnDvdResume");
				bm.set("name", "Resumes BR/DVD");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnImportStart");
				bm.set("name", "Starts Import");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnImportStop");
				bm.set("name", "Stops Import");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnImportPause");
				bm.set("name", "Pauses Import");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnImportResume");
				bm.set("name", "Resumes Import");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnMusicStart");
				bm.set("name", "Starts Music");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnMusicStop");
				bm.set("name", "Stops Music");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnMusicPause");
				bm.set("name", "Pauses Music");
				bm.set("src", model);
				data.add(bm);
				bm = new BaseModel();
				bm.set("event", "notifyOnMusicResume");
				bm.set("name", "Resumes Music");
				bm.set("src", model);
				data.add(bm);
				list.setStore(data);
				list.setDisplayField("name");
				list.addSelectionChangedListener(new SelectionChangedListener<BaseModel>() {

					@SuppressWarnings("unchecked")
					@Override
					public void selectionChanged(SelectionChangedEvent<BaseModel> se) {
						EventType type;
						if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().endsWith("Start"))
							type = EventType.STARTS;
						else if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().endsWith("Stop"))
							type = EventType.STOPS;
						else if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().endsWith("Pause"))
							type = EventType.PAUSES;
						else
							type = EventType.RESUMES;
						MediaType mediaType;
						if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().contains("Tv"))
							mediaType = MediaType.TV;
						else if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().contains("Dvd"))
							mediaType = MediaType.DVD;
						else if(((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("event").toString().contains("Import"))
							mediaType = MediaType.IMPORT;
						else
							mediaType = MediaType.MUSIC;
						final ClientReporterConfigWindow w = new ClientReporterConfigWindow((Client)((BeanModel)((ComboBox<BaseModel>)se.getSource()).getSelection().get(0).get("src")).getBean(), type, mediaType);
						final MessageBox progressBar = MessageBox.wait("Loading configuration window...", "Please wait while the configuration window is loading...", null);
						new Timer() {

							@Override
							public void run() {
								if(w.isBuilt()) {
									this.cancel();
									progressBar.close();
									w.show();
									w.center();
								}
							}

						}.scheduleRepeating(500);
						((ComboBox<BaseModel>)se.getSource()).clear();
					}
				});
				return list;
			}

		};
		rpc = GWT.create(ClientService.class);
		proxy = new RpcProxy<List<Client>>() {

			@Override
			protected void load(Object loadConfig, AsyncCallback<List<Client>> callback) {
				rpc.getClients(callback);
			}

		};
		reader = new BeanModelReader();
		loader = new BaseListLoader<ListLoadResult<Client>>(proxy, reader);
		store = new ListStore<BeanModel>(loader);
		loader.load();
		cols = new ArrayList<ColumnConfig>();
		cols.add(new ColumnConfig("id", "UI Context", 200));

		ColumnConfig cfg = new ColumnConfig("alias", "Alias", 120);
		cfg.setEditor(new CellEditor(new TextField<String>()));
		cols.add(cfg);

		cfg = new ColumnConfig("events", "Client Events", 200);
		cfg.setRenderer(listRenderer);
		cols.add(cfg);

		ColumnModel cm = new ColumnModel(cols);
		grid = new EditorGrid<BeanModel>(store, cm);
		grid.setLazyRowRender(0);
		grid.setSize(620, 285);
		add(grid);

		ToolBar btns = new ToolBar();
		Button save = new Button("Save");
		save.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Client> clnts = new ArrayList<Client>();
				for(Record r : store.getModifiedRecords())
					clnts.add((Client)((BeanModel)r.getModel()).getBean());
				rpc.saveClients(clnts, new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert("ERROR", caught.getLocalizedMessage(), null);
					}

					public void onSuccess(Void result) {
						store.commitChanges();
						MessageBox.alert("Completed", "Clients updated on the server successfully!", null);
					}

				});
			}

		});
		Button cancel = new Button("Cancel");
		cancel.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}

		});
		btns.add(save);
		btns.add(cancel);
		setBottomComponent(btns);
	}	
}
