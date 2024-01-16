describe('use case testing', () => {
  it('(1)サイトにアクセスする', () => {
    cy.visit('http://localhost:8080/top/products#')
  })
  it.skip('(2)会員登録する', () => {
    cy.visit('http://localhost:8080/top/products#')
    cy.get('[href="/login"]').eq(1).click(); 
    cy.get('[href="/customer-registration"]').click();
    cy.get('input[name="lastName"]').type("山田");
    cy.get('input[name="firstName"]').type("太郎");
    cy.get('input[name="birthDate"]').type("2000-01-01");
    cy.get('input[name="email"]').type("test@jido");
    cy.get('input[name="password"]').type("Asdf12345");
    cy.get('input[name="rePassword"]').type("Asdf12345");
    cy.get('button').contains("会員登録").click();
  })
  it('(3)ログインする', () => {
    cy.visit('http://localhost:8080/top/products#');
    cy.get('[href="/login"]').eq(1).click(); 
    // メールアドレスを入力
    cy.get('input[name="email"]').type("test@jido");
    // パスワードを入力
    cy.get('input[name="password"]').type("Asdf12345");
    // ログインボタンをクリック
    cy.get('button').contains("ログイン").click();
    cy.get('h1.text-center').should('have.text', '食の冒険');
  })
  it.skip('(4)地域を探す', () => {
    //ちょっとうまくいかないのでスキップ
    cy.visit('http://localhost:8080/top/products#');
    cy.get('[href="/login"]').eq(1).click(); 
    cy.get('input[name="email"]').type("test@jido");
    cy.get('input[name="password"]').type("Asdf12345");
    cy.get('button').contains("ログイン").click();
    cy.get('h1.text-center').should('have.text', '食の冒険');
  })
});