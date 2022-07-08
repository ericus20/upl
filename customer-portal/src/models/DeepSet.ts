export class DeepSet<T> extends Set<T> {
  add(o: T) {
    // eslint-disable-next-line no-restricted-syntax
    for (const i of this) {
      if (this.deepCompare(o, i)) {
        return this;
      }
    }
    super.add.call(this, o);

    return this;
  }

  // eslint-disable-next-line class-methods-use-this
  private deepCompare(o: T, i: T) {
    return JSON.stringify(o) === JSON.stringify(i);
  }
}
