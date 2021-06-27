using Microsoft.EntityFrameworkCore.Migrations;

namespace DataAccess.Migrations
{
    public partial class TestMigration : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Accounts_Emails_EmailId",
                table: "Accounts");

            migrationBuilder.RenameColumn(
                name: "EmailId",
                table: "Accounts",
                newName: "EmailHolderId");

            migrationBuilder.RenameIndex(
                name: "IX_Accounts_EmailId",
                table: "Accounts",
                newName: "IX_Accounts_EmailHolderId");

            migrationBuilder.AddForeignKey(
                name: "FK_Accounts_Emails_EmailHolderId",
                table: "Accounts",
                column: "EmailHolderId",
                principalTable: "Emails",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Accounts_Emails_EmailHolderId",
                table: "Accounts");

            migrationBuilder.RenameColumn(
                name: "EmailHolderId",
                table: "Accounts",
                newName: "EmailId");

            migrationBuilder.RenameIndex(
                name: "IX_Accounts_EmailHolderId",
                table: "Accounts",
                newName: "IX_Accounts_EmailId");

            migrationBuilder.AddForeignKey(
                name: "FK_Accounts_Emails_EmailId",
                table: "Accounts",
                column: "EmailId",
                principalTable: "Emails",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
