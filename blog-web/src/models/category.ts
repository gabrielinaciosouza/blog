export class Category {
    categoryId: string;
    categoryName: string;
    categoryImageUrl: string;
    categoryColor: string;

    constructor(categoryId: string, categoryName: string, categoryImageUrl: string, categoryColor: string) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImageUrl = categoryImageUrl;
        this.categoryColor = categoryColor;
    }
}
