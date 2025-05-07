package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "Customer address book interface")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * Query all the address information of the currently logged-in user
     *
     * @return operation result with success message
     */
    @GetMapping("/list")
    @ApiOperation("Query all the address information of the currently logged-in user")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * Add new address
     *
     * @param addressBook Address Object
     * @return operation result with success message
     */
    @PostMapping
    @ApiOperation("Add new address")
    public Result<Void> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * Query the address based on the id
     *
     * @param id Address id
     * @return operation result with address's information and success message
     */
    @GetMapping("/{id}")
    @ApiOperation("Query the address based on the id")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * Modify the address based on the id
     *
     * @param addressBook Address Object
     * @return operation result with success message
     */
    @PutMapping
    @ApiOperation("Modify the address based on the id")
    public Result<Void> update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * Set the default address
     *
     * @param addressBook Address Object
     * @return operation result with success message
     */
    @PutMapping("/default")
    @ApiOperation("Set the default address")
    public Result<Void> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * Delete the address based on the id
     *
     * @param id Address id
     * @return operation result with success message
     */
    @DeleteMapping
    @ApiOperation("Delete the address based on the id")
    public Result<Void> deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * Query the default address
     *
     * @return operation result with success or error message
     */
    @GetMapping("default")
    @ApiOperation("Query the default address")
    public Result<AddressBook> getDefault() {
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("The default address was not found");
    }
}