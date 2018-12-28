$(function () {
	var plantJson = JEE.getJson("/master/basPlant/findAll", null, "id", "name");
	var workshopJson = JEE.getJson("/master/basWorkshop/findAll", null, "id", "name");
	var lineJson = JEE.getJson("/master/basLine/findAll", null, "id", "name");
	var typeJson = JEE.getJson("/system/sysDict/findByParentValue", { value: "ULOC_TYPE"}, "value", "name");
	//定义搜索区域字段
	var searchValues = [
		{
			id: "comboSearchTmBasPlantId",
			field: "tmBasPlantId",
			title: "工厂",
			comboUrl: "/master/basPlant/findAll",
			comboData: {enable:1},
			comboId: "id",
			comboText: "name",
			comboChange:function(){
				$.when(JEE.myAjax("/master/basWorkshop/findAll", 
				{tmBasPlantId:$("#comboSearchTmBasPlantId").val(),enable:1})).done(function (data) {
					JEE.initSelect($("#comboSearchTmBasWorkshopId"), data,"id","name");
				});
			}
		},
		{
			id: "comboSearchTmBasWorkshopId",
			field: "tmBasWorkshopId",
			title: "车间",
			comboUrl: "/master/basWorkshop/findAll",
			comboData: {enable:1},
			comboId: "id",
			comboText: "name",
			comboChange:function(){
				$.when(JEE.myAjax("/master/basLine/findAll", 
				{tmBasPlantId:$("#comboSearchTmBasPlantId").val(),
					tmBasWorkshopId:$("#comboSearchTmBasWorkshopId").val(),enable:1}),
				).done(function (data) {
					JEE.initSelect($("#comboSearchTmBasLineId"), data,"id","name");
				});
			}
		},
		{
			id: "comboSearchTmBasLineId",
			field: "tmBasLineId",
			title: "产线",
			comboUrl: "/master/basLine/findAll",
			comboId: "id",
			comboText: "name",
			comboData:{tmBasPlantId:$("#comboSearchTmBasPlantId").val(),
				tmBasWorkshopId:$("#comboSearchTmBasWorkshopId").val(),enable:1}
		},
		{
			id: "comboSearchType",
			field: "type",
			title: "工位类型",
			comboUrl: "/system/sysDict/findByParentValue",
			comboData: { value: "ULOC_TYPE"},
			comboId: "value",
			comboText: "name"
		},
		{
			id: "textSearchNo",
			field: "no",
			title: "工位编号"
		},
		{
			id: "textSearchName",
			field: "name",
			title: "工位名称"
		},
		{
			id: "comboSearchEnable",
			field: "enable",
			title: "启用",
			comboUrl: "/system/sysDict/findByParentValue",
			comboData: { value: "YES_NO"},
			comboId: "value",
			comboText: "name"
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
						$.when(JEE.myAjax("/master/basUloc/delete", {ids: ids})).done(function(result){
							$("#myModal").hide();
							$("#tb_data").bootstrapTable("refresh");
						});
					});
				}
			}
		],
		tableId: "tb_data",
		mainSearch: searchValues,
		url: "/master/basUloc/findByPage",
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
				field: "tmBasPlantId",
				title: "工厂",
				formatter: function (val) {
					if (val) {
						return plantJson[val];
					}
				}
			},
			{
				field: "tmBasWorkshopId",
				title: "车间",
				formatter: function (val) {
					if (val) {
						return workshopJson[val];
					}
				}
			},
			{
				field: "tmBasLineId",
				title: "产线",
				formatter: function (val) {
					if (val) {
						return lineJson[val];
					}
				}
			},
			{
				field: "type",
				title: "工位类型",
				formatter: function(val){
					if(val){
						return typeJson[val];
					}
				}
			},
			{
				field: "no",
				title: "工位编号"
			},
			{
				field: "name",
				title: "工位名称"
			},
			{
				field: "enable",
				title: "是否启用",
				formatter: function (val, row, index) {
					if (val == 1) {
						return '<span class="label label-success arrowed">是</span>';
					} else if (val == 0) {
						return '<span class="label label-danger arrowed-in">否</span>';
					}
				}
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
				var url = change == "edit" ? "/master/basUloc/update" : "/master/basUloc/add";
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
					disable: false,
					comboUrl: "/master/basPlant/findAll",
					comboId: "id",
					comboText: "name",
					comboChange:function(){
						$.when(JEE.myAjax("/master/basWorkshop/findAll", 
						{tmBasPlantId:$("#comboTmBasPlantId").val()})).done(function (data) {
							JEE.initSelect($("#comboTmBasWorkshopId"), data,"id","name");
						});
					},
					valid: "required"
				},
				{
					id: "comboTmBasWorkshopId",
					field: "tmBasWorkshopId",
					title: "车间",
					disable: false,
					comboUrl: "/master/basWorkshop/findAll",
					comboId: "id",
					comboText: "name",
					comboChange:function(){
						$.when(JEE.myAjax("/master/basLine/findAll", 
						{tmBasPlantId:$("#comboTmBasPlantId").val(),tmBasWorkshopId:$("#comboTmBasWorkshopId").val()}),
						).done(function (data) {
							JEE.initSelect($("#comboTmBasLineId"), data,"id","name");
						});
					},
					valid: "required"
				},
				{
					id: "comboTmBasLineId",
					field: "tmBasLineId",
					title: "产线",
					disable: change && change == "edit" ? false : false,
					comboUrl: "/master/basLine/findAll",
					comboId: "id",
					comboText: "name",
					comboData:{tmBasPlantId:$("#comboTmBasPlantId").val(),tmWorkshopId:$("#comboTmBasWorkshopId").val()},
					valid: "required"
				},
				{
					id: "textNo",
					field: "no",
					title: "工位编号",
					disable: change && change == "edit" ? true : false,
					valid: "required"
				},
				{
					id: "textName",
					field: "name",
					title: "工位名称",
					disable: change && change == "edit" ? false : false
				},
				{
					id: "comboType",
					field: "type",
					title: "工位类型",
					disable: change && change == "edit" ? false : false,
					comboUrl: "/system/sysDict/findByParentValue",
					comboData: { value: "ULOC_TYPE"},
					comboId: "value",
					comboText: "name",
					valid: "required"
				},
				{
					id: "switchEnable",
					field: "enable",
					title: "启用",
					disable: change && change == "edit" ? false : false
				},
			]
		}
	});
}
