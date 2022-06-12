interface UserModelType {
  id: string;
  lastName: string;
  firstName: string;
  age: string;
  birthday: any;
}

class UserModel implements UserModelType {
  id = "";
  lastName = "";
  firstName = "";
  age = "";
  birthday = "";

  constructor(init?: Partial<UserModel>) {
    Object.assign(this, init);
  }
}
export default new UserModel();
