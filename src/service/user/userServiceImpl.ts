class UserServiceImpl {
  public setUserModel(UserModel: any, formDataModel: any) {
    Object.assign(UserModel, {
      id: formDataModel.id,
      lastName: formDataModel.lastName,
      firstName: formDataModel.firstName,
      age: formDataModel.age,
      birthday: formDataModel.birthday,
    });
  }

  public clearUserModel(UserModel: any) {
    Object.assign(UserModel, {
      id: "",
      lastName: "",
      firstName: "",
      age: "",
      birthday: "",
    });
  }
}
export default new UserServiceImpl();
