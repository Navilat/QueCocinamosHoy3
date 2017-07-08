class CreateRecipes < ActiveRecord::Migration[5.1]
  def change
    create_table :recipes do |t|
      t.string :name
      t.integer :user_id
      t.integer :category_id
      t.string :ingredients
      t.text :procedure

      t.timestamps
    end
    add_index :recipes, :user_id
    add_index :recipes, :category_id
  end
end
