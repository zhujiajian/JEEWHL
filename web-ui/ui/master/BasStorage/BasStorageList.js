$(function () {
	//定义搜索区域字段
	var searchValues = [
		{
			id: "comboSearchTmBasPlantId",
			field: "tmBasPlantId",
			title: "工厂",
			comboUrl: "/master/basPlant/findAll",
			comboId: "id",
			comboData: {enable:1},
			comboText: "name",
			comboChange:function(){
				$.when(JEE.myAjax("/master/basStorage/findAll",
					{tmBasPlantId:$("#comboSearchTmBasPlantId").val(),enable:1})).done(function (data) {
					JEE.initSelect($("#comboSearchParentId"), data,"id","name");
				});
			}
		},
		{
			id: "comboSearchParentId",
			field: "parentId",
			title: "上级仓库",
			comboUrl: "/master/basStorage/findAll",
			comboId: "id",
			comboText: "name",
			comboData:{tmBasPlantId:$("#comboSearchTmBasPlantId").val(),enable:1}
		},
		{
			id: "comboSearchTmBasDockId",
			field: "tmBasDockId",
			title: "道口",
			comboUrl: "/master/basDock/findAll",
			comboId: "id",
			comboText: "name",
			comboData:{enable:1}
		},
		{
			id: "textSearchTmBasDunitId",
			field: "tmBasDunitId",
			title: "配送单位"
		},
		{
			id: "textSearchNo",
			field: "no",
			title: "仓库编号"
		},
		{
			id: "textSearchType",
			field: "type",
			title: "仓库类型"
		},
		{
			id: "textSearchName",
			field: "name",
			title: "仓库名称"
		},
		{
			id: "textSearchContact",
			field: "contact",
			title: "联系人"
		},
		{
			id: "textSearchEnable",
			field: "enable",
			title: "是否启用"
		},
	];

	//初始化搜索区域
	JEE.initSearch({
		panelTitle: "查询",
		searchValues: searchValues,
		defaultTable: "tb_data" //关联表格id
	});

	//初始化表格
	JEE.initTable({
		btnItems: [{
				btnId: "btn_add",
				btnCss: "btn",
				btnText: "新增",
				spanCss: "glyphicon glyphicon-plus",
				btnClick: function () {
					showDialog();
				}

			},
			{
				btnId: "btn_edit",
				btnCss: "btn btn-primary",
				btnText: "编辑",
				spanCss: "lyphicon glyphicon-pencil",
				btnClick: function () {
					var selections = $("#tb_data").bootstrapTable("getSelections");
					if (selections == null || selections.length != 1) {
						JEE.errMsg("请选择一条需要修改的数据！");
						return;
					}
					showDialog("edit");
				},
				otherOptions: [{
					id: "tb_data",
					selectNum: 1
				}]
			},
			{
				btnId: "btn_delete",
				btnCss: "btn btn-danger",
				btnText: "删除",
				spanCss: "glyphicon glyphicon-remove",
				otherOptions: [{
					id: "tb_data",
					selectMinNum: 1
				}],
				btnClick: function () {
					var selections = $("#tb_data").bootstrapTable("getSelections");
					if (selections == null || selections.length == 0) {
						JEE.errMsg("请选择需要删除的数据！");
						return;
					}
					var ids = [];
					$.each(selections, function (i, item) {
						ids.push(item.id);
					});
					
					JEE.confirmMsg("是否确认删除数据？", function(){
						$.when(JEE.myAjax("/master/basStorage/delete", {ids: ids})).done(function(result){
							$("#myModal").hide();
							$("#tb_data").bootstrapTable("refresh");
						});
					});
				}
			}
		],
		tableId: "tb_data",
		mainSearch: searchValues,
		treeView: true,
		treeField:"no",
		url: "/master/basStorage/findByPage",
		searchParams: function () {
			var temp = {};
			return temp;
		},
		onCheck: function (row, $element) {
		},
		onUncheck: function (row, $element) {
		},
		columns: [{
				checkbox: true
			},
			{
				field: "no",
				title: "仓库编号"
			},
			{
				field: "name",
				title: "仓库名称"
			},
			{
				field: "type",
				title: "仓库类型"
			},
			{
				field: "tmBasPlantId",
				title: "工厂"
			},
			{
				field: "tmBasDockId",
				title: "道口"
			},
			{
				field: "tmBasDunitId",
				title: "配送单位"
			},
			{
				field: "contact",
				title: "联系人"
			},
			{
				field: "enable",
				title: "是否启用"
			},
		]
	});
});

//弹出框
function showDialog(change, formData) {
	var defaultTable = change == "edit" ? "tb_data" : null;
	JEE.initDialog({
		modalId: "myModal",
		modalTitle: change && change == "edit" ? "编辑" : "新增",
		btnItems: [{
			btnId: "btn_save",
			btnCss: "btn btn-primary",
			btnText: "保存",
			formId: "dataForm",
			//showConfirm:true,
			btnClick: function (data) {
				if (change == 'edit') {
					data.id = $("#" + defaultTable).bootstrapTable('getSelections')[0].id;
				}
				var url = change == "edit" ? "/master/basStorage/update" : "/master/basStorage/add";
				$.when(JEE.myAjax(url, data)).done(function (result) {
					if (result) {
						$("#myModal").modal("hide");
						$("#tb_data").bootstrapTable("refresh");
					}
				})
			}
		}],
		modalForm: {
			formId: "dataForm",
			defaultTable: defaultTable,
			formItems: [
				{
					id: "comboTmBasPlantId",
					field: "tmBasPlantId",
					title: "工厂",
					comboUrl: "/master/basPlant/findAll",
					comboId: "id",
					comboData: {enable:1},
					comboText: "name",
					comboChange:function(){
						$.when(JEE.myAjax("/master/basStorage/findAll",
							{tmBasPlantId:$("#comboTmBasPlantId").val(),enable:1})).done(function (data) {
							JEE.initSelect($("#comboParentId"), data,"id","name");
						});
					},
					disable: false,
					valid: "required"
				},
				{
					id: "comboParentId",
					field: "parentId",
					title: "上级仓库",
					comboUrl: "/master/basStorage/findAll",
					comboId: "id",
					comboText: "name",
					comboData:{tmBasPlantId:$("#comboTmBasPlantId").val(),enable:1},
					disable: false
				},
				{
					id: "textNo",
					field: "no",
					title: "仓库编号",
					disable: change && change == "edit" ? true : false,
					valid: "required"
				},
				{
					id: "textName",
					field: "name",
					title: "仓库名称",
					disable: false,
					valid: "required"
				},
				{
					id: "textType",
					field: "type",
					title: "仓库类型",
					disable: false
				},
				{
					id: "textTmBasDockId",
					field: "tmBasDockId",
					title: "道口",
					disable: false
				},
				{
					id: "textTmBasDunitId",
					field: "tmBasDunitId",
					title: "配送单位",
					disable: false
				},
				{
					id: "textContact",
					field: "contact",
					title: "联系人",
					disable: false
				},
				{
					id: "textEnable",
					field: "enable",
					title: "是否启用",
					disable: false
				},
			]
		}
	});
}
