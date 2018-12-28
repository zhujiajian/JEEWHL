$(function () {
	//定义搜索区域字段
	var searchValues = [
		{
			id: "textSearchName",
			field: "name",
			title: "姓名"
		},
		{
			id: "comboSearchEnable",
			field: "enable",
			title: "启用",
			comboUrl: "/system/sysDict/findByParentValue",
			comboData: {
				value: "YES_NO"
			},
			comboId: "value",
			comboText: "name"
		}
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
				btnClick: function () {
					showDialog();
				}

			},
			{
				btnId: "btn_edit",
				btnCss: "btn btn-primary",
				btnText: "编辑",
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
						$.when(JEE.myAjax("/system/sysUser/delete", {ids: ids})).done(function(result){
							$("#myModal").hide();
							$("#tb_data").bootstrapTable("refresh");
						});
					});
				}
			}
		],
		tableId: "tb_data",
		mainSearch: searchValues,
		url: "/system/sysUser/findByPage",
		//pagination: false,
		singleSelect:true,
		searchParams: function () {
			var temp = {};
			return temp;
		},
		columns: [{
				checkbox: true
			},
			{
				field: 'loginName',
				title: '用户名'
			},
			{
				field: 'name',
				title: '姓名',
				width:200
			},

			{
				field: 'no',
				title: '工号'
			},
			{
				field: 'officeId',
				title: '归属部门'
			},
			{
				field: 'email',
				title: '邮箱'
			},
			{
				field: 'phone',
				title: '电话'
			},
			{
				field: 'photo',
				title: '用户头像'
			},
			{
				field: "enable",
				title: "启用",
				formatter: function (val, row, index) {
					if (val == 1) {
						return '<span class="label label-success arrowed">是</span>';
					} else if (val == 0) {
						return '<span class="label label-danger arrowed-in">否</span>';
					}
				}
			},
			{
				field: "remark",
				title: "备注信息"
			}
		],
		onCheck: function (row, $element) {
			$("#tb_role").bootstrapTable("refresh");
		},
		onUncheck: function (row, $element) {
			$("#tb_role").bootstrapTable("refresh");
		}
	});
	
	//角色子表
	JEE.initTable({
		btnItems: [{
				btnId: "btn_addSub",
				btnCss: "btn",
				btnText: "新增",
				btnClick: function () {
					showSubDialog();
				}

			},
			{
				btnId: "btn_deleteSub",
				btnCss: "btn btn-danger",
				btnText: "删除",
				otherOptions: [{
					id: "tb_data",
					selectNum:1
				},{
					id: "tb_role",
					selectMinNum: 1
				}],
				btnClick: function () {
					var users = $("#tb_data").bootstrapTable("getSelections");
					if(users == null || users.length !=1){
						JEE.errMsg("请选择用户！");
						return;
					}
					
					var selections = $("#tb_role").bootstrapTable("getSelections");
					if (selections == null || selections.length == 0) {
						JEE.errMsg("请选择需要删除的数据！");
						return;
					}
					var ids = [];
					$.each(selections, function (i, item) {
						ids.push(item.id);
					});
					
					JEE.confirmMsg("是否确认删除数据？", function(){
						$.when(JEE.myAjax("/system/sysUser/deleteRoleByUser", {userId:users[0].id,roleIds: ids})).done(function(result){
							$("#tb_role").bootstrapTable("refresh");
						});
					});
				}
			}
		],
		tableId: "tb_role",
		//mainSearch: searchValues,
		url: "/system/sysRole/findByUser",
		pagination: false,
		//singleSelect:true,
		searchParams: function () {
			var temp = {userId: $("#tb_data").bootstrapTable('getSelections')[0] ? $("#tb_data").bootstrapTable('getSelections')[0].id : -1};
			return temp;
		},
		columns: [{
				checkbox: true
			},
			{
				field: "no",
				title: "角色编码"
			},
			{
				field: "name",
				title: "角色名称"
			}
		],
		onCheck: function (row, $element) {
		},
		onUncheck: function (row, $element) {
		},
	});
});

//弹出框
function showDialog(change) {
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
				var url = change == "edit" ? "/system/sysUser/update" : "/system/sysUser/add";
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
					id: "textLoginName",
					field: "loginName",
					title: "用户名",
					disable: change && change=="edit"?true:false,
					valid: "required"
				},
				{
					id: "textNo",
					field: "no",
					title: "工号"
				},
				{
					id: "textName",
					field: "name",
					title: "姓名",
					valid: "required"
				},
				{
					id: "textEmail",
					field: "email",
					title: "邮箱",
					disable: change && change=="edit"?true:false,
					valid: {
						required:true,
						email:true
					}
				},
				{
					id: "comboOffice",
					field: "officeId",
					title: "部门",
					comboUrl: "/sysOffice/findAll",
					comboId: "id",
					comboText: "name"
				},
				{
					id: "textPhone",
					field: "phone",
					title: "联系方式",
					valid: "required"
				},
				{
					id: "textPhoto",
					field: "photo",
					title: "用户图像"
				},
				{
					id: "areaRemark",
					field: "remark",
					title: "备注"
				}
			]
		}
	});
}

//弹出框
function showSubDialog(change) {
	JEE.initDialog({
		modalId: "myModal",
		modalTitle: "新增角色",
		btnItems: [{
			btnId: "btn_save",
			btnCss: "btn btn-primary",
			btnText: "保存",
			formId: "dataForm",
			//showConfirm:true,
			btnClick: function (data) {
				if(!$("#tb_data").bootstrapTable('getSelections')[0]){
					JEE.errMsg("请选择需要授权的用户！");
					return;
				}
				
				data.id = $("#tb_data").bootstrapTable('getSelections')[0] ? $("#tb_data").bootstrapTable('getSelections')[0].id : -1;
				var url = "/system/sysUser/grantUser";
				$.when(JEE.myAjax(url, data)).done(function (result) {
					if (result) {
						$("#myModal").modal("hide");
						$("#tb_role").bootstrapTable("refresh");
					}
				})
			}
		}],
		modalForm: {
			formId: "dataForm",
			formItems: [
				{
					id: "comboRole",
					field: "roleIds",
					title: "角色",
					comboUrl: "/system/sysRole/findAll",
					comboId: "id",
					comboText: "name",
					multiple: true
				}
			]
		}
	});
}