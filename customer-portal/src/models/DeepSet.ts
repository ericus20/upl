/**
 * A custom Set that extends the TypeScript Set and provide a deepCompare function
 * more like the equals method to be used to check for equality.
 */
export class DeepSet<T> extends Set<T> {
  /**
   * Overriding the add function in the default Set.
   *
   * @param data the data to add to set
   * @returns
   */
  add(data: T) {
    // eslint-disable-next-line no-restricted-syntax
    for (const i of this) {
      if (this.deepCompare(data, i)) {
        return this;
      }
    }
    super.add.call(this, data);

    return this;
  }

  // eslint-disable-next-line class-methods-use-this
  private deepCompare(obj1: T, obj2: T) {
    return JSON.stringify(obj1) === JSON.stringify(obj2);
  }
}
