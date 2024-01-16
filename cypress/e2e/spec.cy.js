describe('ユースケース(5)以降', () => {
  beforeEach(() => {
    //会員登録済みの会員情報でログイン
    cy.visit('http://localhost:8080/top/products#');
    cy.get('[href="/login"]').eq(1).click(); 
    cy.get('input[name="email"]').type("test@jido");
    cy.get('input[name="password"]').type("Asdf12345");
    cy.get('button').contains("ログイン").click();
    cy.get('h1.text-center').should('have.text', '食の冒険');
  });
  it.skip('(5)地域検索をする', () => {
    //「沖縄」と入力して検索
    cy.get('#query').type('沖縄');
    cy.get('button[type="submit"]').click();
    // 「沖縄そば」の商品が表示されていることを確認
    cy.get('span').should('contain', '沖縄そば');
    // 他の商品（例：ゴーヤチャンプル）が表示されていないことを確認
    cy.get('span').should('not.contain', 'ゴーヤチャンプル');
  })
  it.skip('(6)イベント情報をみる', () => {
    // ページ内の "北海道食材フェア" をクリック
    cy.contains('北海道食材フェア').click();
    // 新しいページに遷移したことを確認
    cy.url().should('include', '/event/1'); 
    // 新しいページに表示される要素を確認
    cy.get('.product-detail').should('be.visible');
    cy.contains('イベント画面').should('be.visible');
    cy.get('input[name="eventId"]').should('have.value', '1'); 

  })
  it.skip('(7)ショッピングカートに追加する', () => {
    // 商品詳細ページに遷移
    cy.get('a[href="/top/showDetail/21"]').eq(0).click();
    // 商品詳細画面が表示されていることを確認
    cy.get('.product-detail').should('be.visible');
    // 「ショッピングカートに追加」ボタンをクリック
    cy.get('.product-detail-to-cart-button').click();
    //商品カート画面が表示されていることを確認
    cy.contains('沖縄そば').should('be.visible');
    cy.contains('700円').should('be.visible');
    cy.contains('沖縄独特のソウルフードで、麺が太くて美味しいです。').should('be.visible');
  })
  it.skip('(8)ショッピングカート内の商品を削除する', () => {
    // （7）と同様にカートに商品追加
    cy.get('a[href="/top/showDetail/21"]').eq(0).click();
    cy.get('.product-detail').should('be.visible');
    cy.get('.product-detail-to-cart-button').click();
    // 商品を削除
    cy.get('.shopping-cart-btn').click();
    // 削除後のメッセージ画面が表示されていることを確認
    // ここの画面表示が手動の時と異なる気がする。。。
    cy.get('.main-shopping-cart').should('be.visible');
    cy.contains('商品はありません。').should('be.visible');
    cy.contains('戻る').should('be.visible');

  })
  it('(9)商品を購入する', () => {
    // 沖縄そばをカートに追加
    cy.get('a[href="/top/showDetail/21"]').eq(0).click();
    // 「ショッピングカートに追加」ボタンをクリック
    cy.get('.product-detail-to-cart-button').click();
   
    // ボタンをクリックしてモック関数を呼び出す
    cy.get('#orderConfirmationForm button').click();

    // テストデータを入力（例：名前、住所など）
    cy.get('input[name="name"]').type('テストユーザー');
    cy.get('input[name="address"]').type('テスト住所');
    cy.get('input[name="email"]').type('test@example.com');

    // 確定ボタンをクリック
    cy.get('button[type="submit"]').click();
});

  it.skip('(10)ログアウト状態から商品を購入する', () => {
    
  })
  it.skip('(11)会員未登録状態から商品を購入する', () => {
    
  })
  it.skip('(12)ショッピングカートに追加してサイトを離れた後再度アクセスして購入する', () => {
   
  })
  it.skip('(13)ログイン状態のカートなの商品とログアウト状態からカートに追加した商品が両方購入する', () => {
   
  })
  it.skip('14)ログイン状態のカート内の商品とログアウト状態からカートに追加した商品が両方購入する', () => {
    
  })
  it.skip('15)ログイン後、ブラウザバック実施してタイトル押下でTOP画面に戻る', () => {
    
  })
});
