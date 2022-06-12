interface UserDtoType {
  id: string;
  file: Blob;
}

class UserDto implements UserDtoType {
  public id = "";
  public file = new Blob();

  constructor(init?: Partial<UserDto>) {
    Object.assign(this, init);
  }
}
export default new UserDto();
