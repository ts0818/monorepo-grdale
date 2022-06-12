import { VNode, CreateElement } from 'vue';
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class HelloWorld extends Vue {
  /** State */
  public world: string = 'world';

  /** View(renderメソッド) */
  public render(h: CreateElement): VNode {
    /** Style */
    const style = {
      fontSize: '64px',
      color: '#3ab981',
    };

    /** Template(JSX) */
    return <h1 style={style}>{this.world}</h1>;
  }

  private createHtmlDivElement() {

  }
}
